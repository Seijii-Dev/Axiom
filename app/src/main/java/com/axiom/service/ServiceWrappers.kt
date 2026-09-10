package com.axiom.service

import com.axiom.api.data.editor.WorkspaceTab
import com.axiom.api.data.preferences.AppSettings
import com.axiom.api.data.preferences.AppearanceSettings
import com.axiom.api.data.preferences.EditorSettings
import com.axiom.api.data.preferences.FileTreeSettings
import com.axiom.api.data.preferences.PluginSettingsData
import com.axiom.api.data.preferences.TerminalSettings
import com.axiom.api.service.Fonts
import com.axiom.api.service.Settings
import com.axiom.api.service.Tabs
import com.axiom.data.preferences.FontManager
import com.axiom.data.preferences.SettingsRepository
import com.axiom.presentation.viewmodel.EditorViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class SettingsWrapper(private val repo: SettingsRepository) : Settings {

    override val settings: Flow<AppSettings> = repo.settings

    override suspend fun updateSettings(transform: (AppSettings) -> AppSettings) {
        repo.updateSettings(transform)
    }

    override suspend fun updateAppearanceSettings(transform: (AppearanceSettings) -> AppearanceSettings) {
        repo.updateAppearanceSettings(transform)
    }

    override suspend fun updateTerminalSettings(transform: (TerminalSettings) -> TerminalSettings) {
        repo.updateTerminalSettings(transform)
    }

    override suspend fun updateFileTreeSettings(transform: (FileTreeSettings) -> FileTreeSettings) {
        repo.updateFileTreeSettings(transform)
    }

    override suspend fun updateEditorSettings(transform: (EditorSettings) -> EditorSettings) {
        repo.updateEditorSettings(transform)
    }

    override suspend fun getPluginSettings(pluginId: String): PluginSettingsData {
        return repo.getPluginSettings(pluginId)
    }

    override suspend fun updatePluginSettings(pluginId: String, transform: (PluginSettingsData) -> PluginSettingsData) {
        repo.updatePluginSettings(pluginId, transform)
    }
}

@Single
class FontsWrapper(private val manager: FontManager) : Fonts {
    override suspend fun getFontFamily(uri: String?) = manager.getFontFamily(uri)

    override fun clearCache() {
        manager.clearCache()
    }
}

class TabsWrapper(private val editorViewModelProvider: () -> EditorViewModel) : Tabs {

    private val editorViewModel by lazy { editorViewModelProvider() }

    override val current: WorkspaceTab?
        get() = editorViewModel.activeTab.value

    override val opened: List<WorkspaceTab>
        get() = editorViewModel.openTabs.value.toList()

    override fun open(tab: WorkspaceTab) {
        editorViewModel.openTab(tab)
    }

    override fun close(id: String) {
        editorViewModel.closeTab(id)
    }

    override fun select(id: String) {
        editorViewModel.selectTab(id)
    }

    override fun get(id: String): WorkspaceTab? {
        return editorViewModel.openTabs.value.find { it.id == id }
    }
}


