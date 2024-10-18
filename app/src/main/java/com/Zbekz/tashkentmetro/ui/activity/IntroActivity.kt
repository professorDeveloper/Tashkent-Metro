package com.Zbekz.tashkentmetro.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.Zbekz.tashkentmetro.R
import com.Zbekz.tashkentmetro.databinding.ActivityIntroBinding
import com.Zbekz.tashkentmetro.utils.initActivity
import com.Zbekz.tashkentmetro.utils.visible
import com.Zbekz.tashkentmetro.viewmodel.imp.IntroViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private val model by viewModels<IntroViewModelImpl>()
    private lateinit var viewBinding: ActivityIntroBinding
    private lateinit var host: NavHostFragment
    private lateinit var graph: NavGraph


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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