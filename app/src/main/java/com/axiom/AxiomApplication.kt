package com.axiom

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import com.axiom.api.InternalAxiomApi
import com.axiom.api.NavDestination
import com.axiom.api.Navigator
import com.axiom.api.data.editor.FileOpenRequest
import com.axiom.api.data.editor.FileOpener
import com.axiom.api.data.editor.FileOpenerRegistration
import com.axiom.api.data.editor.FileOpenerRegistry
import com.axiom.api.data.editor.WorkspaceTab
import com.axiom.api.data.fs.FileSystem
import com.axiom.api.data.fs.Paths
import com.axiom.api.data.fs.pluginsDir
import com.axiom.api.data.runner.FileRunner
import com.axiom.api.data.runner.FileRunRequest
import com.axiom.api.data.runner.FileRunnerRegistration
import com.axiom.api.data.runner.FileRunnerRegistry
import com.axiom.api.data.terminal.TerminalManager
import com.axiom.api.data.terminal.TerminalSessionBinder
import com.axiom.api.data.terminal.TerminalSessionManager
import com.axiom.api.event.EventBusHolder
import com.axiom.api.language.LanguageRegistry
import com.axiom.api.lsp.LanguageServerRegistry
import com.axiom.api.plugin.AxiomPlugin
import com.axiom.api.plugin.PluginInfo
import com.axiom.api.plugin.PluginSettings
import com.axiom.api.plugin.PluginSettingsRegistration
import com.axiom.api.plugin.PluginSettingsRegistry
import com.axiom.api.plugin.info
import com.axiom.api.service.Logger
import com.axiom.api.ui.Content
import com.axiom.api.ui.Screen
import com.axiom.api.ui.ScreenId
import com.axiom.api.ui.ScreenRegistration
import com.axiom.api.ui.ScreenRegistry
import com.axiom.api.ui.ToolbarAction
import com.axiom.api.ui.ToolbarIcon
import com.axiom.api.ui.ToolbarRegistration
import com.axiom.api.ui.ToolbarRegistry
import com.axiom.language.LanguageRegistryImpl
import com.axiom.core.App
import com.axiom.core.initApp
import com.axiom.data.terminal.DefaultTerminalSessionManager
import com.axiom.data.terminal.TerminalSessionBinderImpl
import com.axiom.data.runner.PythonFileRunner
import com.axiom.data.runner.TerminalCommandRunner
import com.axiom.data.preferences.SettingsDataStore
import com.axiom.data.repository.TrashRepository
import com.axiom.di.AppModule
import com.axiom.event.eventBus
import com.axiom.event.initializeGlobalEventBus
import com.axiom.plugin.PluginManager
import com.axiom.service.FontsWrapper
import com.axiom.service.SettingsWrapper
import com.axiom.service.TabsWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.plugin.module.dsl.startKoin

@KoinApplication(modules = [AppModule::class])
class AxiomApplication : Application() {

    lateinit var app: App
        private set

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        CrashHandler.install(this)
        System.loadLibrary("axiom")

        startKoin<AxiomApplication> {
            androidLogger()
            androidContext(this@AxiomApplication)
        }

        app = initApp()
        initializeGlobals()
        scheduleTrashPurge()
    }

    private fun scheduleTrashPurge() {
        applicationScope.launch(Dispatchers.IO) {
            runCatching {
                val retention = auto<SettingsDataStore>().data.first().fileTree.trashRetentionDays
                auto<TrashRepository>().purgeExpired(retention)
            }.onFailure { Log.w("Trash", "Startup purge failed", it) }
        }
    }

    @OptIn(InternalAxiomApi::class)
    private fun initializeGlobals() {
        initializeGlobalEventBus(app)
        app.setGlobal(EventBusHolder(app.eventBus()))

        val terminalManager = TerminalManagerImpl(
            sessionBinder = TerminalSessionBinderImpl(),
            sessionManager = DefaultTerminalSessionManager(),
            terminalRunner = auto(),
            app = app,
        )
        app.setGlobal(terminalManager)
        app.setGlobal(auto<FileSystem>())
        app.setGlobal(MutableScreenRegistry())
        app.setGlobal(MutableToolbarRegistry())
        app.setGlobal(MutableFileRunnerRegistry().apply {
            registerInternal(PythonFileRunner())
        })
        app.setGlobal(MutableFileOpenerRegistry())
        app.setGlobal(MutablePluginSettingsRegistry())
        app.setGlobal(SettingsWrapper(auto()))
        app.setGlobal(FontsWrapper(auto()))
        app.setGlobal(TabsWrapper { auto() })
        app.setGlobal(PluginManager(app))
        app.setGlobal(auto<LanguageServerRegistry>())
        app.setGlobal(LanguageRegistryImpl(this))
        app.setGlobal(auto<Logger>())
    }

    private class TerminalManagerImpl(
        override val sessionManager: TerminalSessionManager,
        override val sessionBinder: TerminalSessionBinder,
        private val terminalRunner: TerminalCommandRunner,
        private val app: App,
    ) : TerminalManager {

        override suspend fun runInTerminal(
            command: String,
            cwd: String?,
            sessionName: String?,
        ) {
            terminalRunner.run(
                navigateToTerminal = { app.global<Navigator>().navigateTo(NavDestination.Terminal) },
                command = command,
                cwd = cwd,
                sessionName = sessionName,
            )
        }

        override fun openTerminal() {
            app.global<Navigator>().navigateTo(NavDestination.Terminal)
        }
    }

    private inline fun <reified T> auto(): T = GlobalContext.get().get()

    private class MutableScreenRegistry : ScreenRegistry {

        private val screens = mutableStateMapOf<ScreenId, Content>()
        private val transientScreens = mutableMapOf<ScreenId, Content>()
        private val screenOwner = mutableMapOf<ScreenId, String>()

        context(plugin: AxiomPlugin)
        override fun register(screen: Screen): ScreenRegistration {
            transientScreens.remove(screen.id)
            screens[screen.id] = screen.content
            screenOwner[screen.id] = plugin.info.id
            return ScreenRegistration {
                screens.remove(screen.id)
                screenOwner.remove(screen.id)
            }
        }

        override fun unregister(id: ScreenId) {
            transientScreens.remove(id)
            screens.remove(id)
            screenOwner.remove(id)
        }

        override fun set(id: ScreenId, content: Content) {
            transientScreens.remove(id)
            screens[id] = content
        }

        override fun setTransient(id: ScreenId, content: Content) {
            transientScreens[id] = content
        }

        override fun unregisterTransient(id: ScreenId) {
            transientScreens.remove(id)
        }

        override fun get(id: ScreenId): Content? {
            return transientScreens[id] ?: screens[id]
        }

        override fun ownerOf(id: ScreenId): String? = screenOwner[id]

        @InternalAxiomApi
        override fun unregisterAll(pluginId: String) {
            val toRemove = screenOwner.filterValues { it == pluginId }.keys.toList()
            toRemove.forEach { unregister(it) }
        }
    }

    private class MutableToolbarRegistry : ToolbarRegistry {
        private val _actions = mutableStateListOf<ToolbarAction>()

        context(plugin: AxiomPlugin)
        override fun register(action: ToolbarAction): ToolbarRegistration {
            val resolved = action.resolve(plugin.info)
            _actions += resolved
            return ToolbarRegistration { _actions.remove(resolved) }
        }

        fun ToolbarAction.resolve(info: PluginInfo): ToolbarAction {
            val resolved = when (val icon = icon) {
                is ToolbarIcon.Resource -> {
                    val file = Paths.pluginsDir
                        .resolve(info.id)
                        .resolve(icon.path)

                    if (file.exists()) {
                        ToolbarIcon.File(file)
                    } else {
                        Log.w("ToolbarRegistry", "Plugin '${info.id}' references missing icon '${icon.path}'.")
                        null
                    }
                }

                else -> icon
            }

            return copy(icon = resolved)
        }

        override fun unregister(id: String) {
            _actions.removeAll { it.id == id }
        }

        override fun actions(): List<ToolbarAction> {
            return _actions
        }
    }

    private class MutableFileRunnerRegistry : FileRunnerRegistry {

        private val _runners = mutableStateListOf<FileRunner>()
        private val _sortedRunners = mutableStateListOf<FileRunner>()
        private val _owners = mutableMapOf<String, String>()

        private fun updateSortedRunners() {
            _sortedRunners.clear()
            _sortedRunners.addAll(_runners.sortedByDescending { it.priority })
        }

        context(plugin: AxiomPlugin)
        override fun register(runner: FileRunner): FileRunnerRegistration {
            _runners.removeAll { it.id == runner.id }
            _runners += runner
            updateSortedRunners()
            _owners[runner.id] = plugin.info.id
            return FileRunnerRegistration {
                _runners.removeAll { it.id == runner.id }
                updateSortedRunners()
                _owners.remove(runner.id)
            }
        }

        /**
         * Registers a built-in runner that is not owned by any plugin, so it is never removed
         * by [unregisterAll].
         */
        fun registerInternal(runner: FileRunner): FileRunnerRegistration {
            _runners.removeAll { it.id == runner.id }
            _runners += runner
            updateSortedRunners()
            return FileRunnerRegistration {
                _runners.removeAll { it.id == runner.id }
                updateSortedRunners()
                _owners.remove(runner.id)
            }
        }

        override fun unregister(id: String) {
            _runners.removeAll { it.id == id }
            updateSortedRunners()
            _owners.remove(id)
        }

        override fun runnerFor(request: FileRunRequest): FileRunner? =
            runners().firstOrNull { runCatching { it.supports(request) }.getOrDefault(false) }

        override fun supports(request: FileRunRequest): Boolean =
            runnerFor(request) != null

        override fun runners(): List<FileRunner> =
            _sortedRunners

        @InternalAxiomApi
        override fun unregisterAll(pluginId: String) {
            val toRemove = _owners.filterValues { it == pluginId }.keys.toList()
            toRemove.forEach { unregister(it) }
        }
    }

    private class MutableFileOpenerRegistry : FileOpenerRegistry {

        private val _openers = mutableStateListOf<FileOpener>()
        private val _sortedOpeners = mutableStateListOf<FileOpener>()

        private fun updateSortedOpeners() {
            _sortedOpeners.clear()
            _sortedOpeners.addAll(_openers.sortedByDescending { it.priority })
        }

        context(plugin: AxiomPlugin)
        override fun register(opener: FileOpener): FileOpenerRegistration {
            _openers.removeAll { it.id == opener.id }
            _openers += opener
            updateSortedOpeners()
            return FileOpenerRegistration {
                _openers.removeAll { it.id == opener.id }
                updateSortedOpeners()
            }
        }

        override fun unregister(id: String) {
            _openers.removeAll { it.id == id }
            updateSortedOpeners()
        }

        override fun openers(): List<FileOpener> =
            _sortedOpeners

        override suspend fun open(request: FileOpenRequest): WorkspaceTab? {
            for (opener in openers()) {
                val tab = opener.open(request)
                if (tab != null) return tab
            }
            return null
        }
    }

    private class MutablePluginSettingsRegistry : PluginSettingsRegistry {

        private val content = mutableStateMapOf<String, @Composable PluginSettings.() -> Unit>()

        context(plugin: AxiomPlugin)
        override fun register(
            content: @Composable PluginSettings.() -> Unit
        ): PluginSettingsRegistration {
            val pluginId = plugin.info.id
            this.content[pluginId] = content
            return PluginSettingsRegistration { this@MutablePluginSettingsRegistry.content.remove(pluginId) }
        }

        override fun hasSettings(pluginId: String): Boolean = content.containsKey(pluginId)

        @InternalAxiomApi
        override fun contentFor(pluginId: String): (@Composable PluginSettings.() -> Unit)? =
            content[pluginId]

        @InternalAxiomApi
        override fun unregisterAll(pluginId: String) {
            content.remove(pluginId)
        }
    }
}
