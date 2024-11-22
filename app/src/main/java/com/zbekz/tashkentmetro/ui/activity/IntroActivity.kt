package com.zbekz.tashkentmetro.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.databinding.ActivityIntroBinding
import com.zbekz.tashkentmetro.utils.ViewUtils
import com.zbekz.tashkentmetro.utils.initActivity
import com.zbekz.tashkentmetro.viewmodel.imp.IntroViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private val model by viewModels<IntroViewModelImpl>()
    private lateinit var viewBinding: ActivityIntroBinding
    private lateinit var host: NavHostFragment
    private lateinit var graph: NavGraph

    @Inject
    lateinit var userPreferenceManager: AppReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityIntroBinding.inflate(layoutInflater)
        ViewUtils.setLanguageForService(this, userPreferenceManager)
        setContentView(viewBinding.root)
        initActivity(this)

//        host = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
//        graph = host.navController.navInflater.inflate(R.navigation.intro_graph)
//
//        host.navController.graph = graph
    }
}