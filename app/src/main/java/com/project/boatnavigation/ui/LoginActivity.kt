package com.project.boatnavigation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.boatnavigation.MapsActivity
import com.project.boatnavigation.R
import com.project.boatnavigation.controller.BaseActivity
import com.project.boatnavigation.customview.button.RipplesButton
import com.project.boatnavigation.customview.edittext.FloatingEditText
import com.project.boatnavigation.manager.Utils
import com.project.boatnavigation.manager.preference.AppPreference

class LoginActivity : BaseActivity() {
    var loginButton: RipplesButton? = null
    var editTextUserName: FloatingEditText? = null
    var editTextPassword: FloatingEditText? = null
    var textViewSignin: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setAction()
    }

    private fun setAction() {
        loginButton!!.setOnClickListener(RipplesButton.OnClickListener { onLogin() })
        textViewSignin!!.setOnClickListener(View.OnClickListener {
            navigateToRegister();

        })
    }

    fun navigateToRegister() {
//        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//        startActivity(intent)
    }

    private fun init() {
        editTextUserName = findViewById<View>(R.id.editTextName) as FloatingEditText
        loginButton = findViewById<View>(R.id.loginButton) as RipplesButton
        editTextPassword = findViewById<View>(R.id.editTextProblem) as FloatingEditText
        textViewSignin = findViewById<View>(R.id.textViewSignin) as TextView
        editTextUserName!!.setText("Elwin")
        editTextPassword!!.setText("pass")
        FirebaseApp.initializeApp(this)
        //Checkout.preload(applicationContext)
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_login_activity
    }

    override fun getActivityId(): String {
        return "LoginActivity"
    }

    fun onLogin() {
        if (editTextUserName!!.text.toString().isEmpty() ||
            editTextPassword!!.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "Please fill all mandatory fields", Toast.LENGTH_LONG).show()
            return
        }
        val pDialog = Utils.getProgressDialog(this)
        pDialog.show()
        val database = FirebaseDatabase.getInstance(DB_URL)
        val myRef = database.getReference("usert").child("users")
        myRef.orderByChild("username")
            .equalTo(editTextUserName!!.text.toString().trim { it <= ' ' })
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    pDialog.dismiss()
                    var auth = false
                    for (ds in dataSnapshot.children) {
                        val password = ds.child("password").getValue(
                            String::class.java
                        )
                        if (password == editTextPassword!!.text.toString()) {
                            auth = true
                            val mobile = ds.child("mobile").getValue(
                                String::class.java
                            )
                            //AppPreference.setUserMobile(mobile, this@LoginActivity);
                            //AppPreference.setUserId(editTextUserName!!.text.toString().trim(), this@LoginActivity);
                        }
                    }
                    if (auth) {
                        val intent = Intent(this@LoginActivity, MapsActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    pDialog.dismiss()
                    Toast.makeText(this@LoginActivity, "Login error", Toast.LENGTH_LONG).show()
                    throw databaseError.toException()
                }
            })
    }

    override fun searchInitialClicked() {}
    override fun searchBackClicked() {}
    override fun onBackButtonCLick() {
        finish()
    }

    override fun onSearchButtonClick(key: String) {}
    override fun onMenuItemButtnCalick() {}
    override fun onMenuMainButtonClick() {}
    override fun setActionBarTitle(): Int {
        return R.string.login_title_bar_text
    }

    override fun setToolBarIcons(): IntArray {
        return intArrayOf(R.drawable.logo_back, 0, 0, 0)
    }
}