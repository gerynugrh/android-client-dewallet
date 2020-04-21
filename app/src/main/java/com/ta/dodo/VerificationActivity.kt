package com.ta.dodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ta.dodo.ui.verification.IdentityFragment

class VerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verification_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, IdentityFragment.newInstance())
                .commitNow()
        }
    }
}
