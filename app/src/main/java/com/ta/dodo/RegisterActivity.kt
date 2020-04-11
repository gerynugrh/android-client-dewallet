package com.ta.dodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ta.dodo.ui.register.RegisterFragment

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, RegisterFragment.newInstance())
                    .commitNow()
        }
    }
}
