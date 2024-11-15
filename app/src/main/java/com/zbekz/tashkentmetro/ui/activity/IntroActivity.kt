package com.zbekz.tashkentmetro.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.databinding.ActivityIntroBinding
import com.zbekz.tashkentmetro.utils.ViewUtils
import com.zbekz.tashkentmetro.utils.initActivity
import com.zbekz.tashkentmetro.utils.visible
import com.zbekz.tashkentmetro.viewmodel.imp.IntroViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    @Inject
    lateinit var userPreferenceManager: AppReference

    private val model by viewModels<IntroViewModelImpl>()
    private lateinit var viewBinding: ActivityIntroBinding
    private lateinit var host: NavHostFragment
    private lateinit var graph: NavGraph


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setLanguageForService(this, userPreferenceManager)

        viewBinding = ActivityIntroBinding.inflate(layoutInflater)


        host = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        graph = host.navController.navInflater.inflate(R.navigation.intro_graph)
        model.loginScreenLiveData.observe(this@IntroActivity, getStartedScreenObserver)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            model.checkPage()
        }else {
            graph.setStartDestination(R.id.splashPage)
            viewBinding.navHost.visible()
        }
        setContentView(viewBinding.root)
        initActivity(this)


    }

    private val openHomeScreenObserver = Observer<Unit> {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private val getStartedScreenObserver = Observer<Unit> {
        viewBinding.navHost.visible()
        graph.setStartDestination(R.id.firstPage)
    }
}