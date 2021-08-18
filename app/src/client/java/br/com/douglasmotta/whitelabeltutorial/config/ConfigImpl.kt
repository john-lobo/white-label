package br.com.douglasmotta.whitelabeltutorial.config

import android.view.View
import br.com.douglasmotta.whitelabeltutorial.config.Config
import javax.inject.Inject

class ConfigImpl @Inject constructor() :Config {
    override val addButtonVisibility: Int = View.GONE
}