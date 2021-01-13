package com.example.ossquiz.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ossquiz.R
import com.example.ossquiz.models.User
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.toolbar

class MainActivity : AppCompatActivity() {
    lateinit var _MyUser : User
    val _ID : Int = 117
    var _LastScrore : Int = 0
    lateinit var _Preferences : SharedPreferences
    val PREF_KEY_SCORE : String = "PREF_KEY_SCORE"
    val PREF_KEY_NAME : String =  "PREF_KEY_NAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureImageView()
        configureToolBar()

        _Preferences = getPreferences(Context.MODE_PRIVATE)

        ChargeUser()

        editName.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!editName.text.isNullOrBlank())
                    btnPlay.isEnabled = true
            }
        })

        btnPlay.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                _MyUser  = User(editName.text.toString())

                _Preferences.edit().putString(PREF_KEY_NAME, _MyUser._Name).apply()
                val _IntentQuestionActivity  = Intent(this@MainActivity, QuestionActivity::class.java)
                _IntentQuestionActivity.putExtra("User",_MyUser._OSS)
                startActivityForResult(_IntentQuestionActivity,_ID)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (_ID == requestCode && Activity.RESULT_OK == resultCode)
            if (_MyUser._OSS)
            {
                _Preferences.edit().putBoolean("OSS", _MyUser._OSS).apply()
            }
            else if (data != null)
            {
                _LastScrore = data.getIntExtra(Constants.KEY_SCORES,0)
                _Preferences.edit().putBoolean("OSS", _MyUser._OSS).apply()
                _Preferences.edit().putInt(PREF_KEY_SCORE,_LastScrore).apply()
            }
    }
    private fun ChargeUser() {
        val oss = _Preferences.getBoolean("OSS", false)
        val name = _Preferences.getString(PREF_KEY_NAME, null)

        if (null != name)
        {
            if(oss)
                {
                val fulltext = """
                Bon retour parmi nous, OSS 117!
                Votre dernière mission a été un succès
                """.trimIndent()
                txtHome.setText(fulltext)
            }
            else
            {
                val score: Int = _Preferences.getInt(PREF_KEY_SCORE, 0)
                val fulltext = """
                Bon retour, $name!
                Votre dernier score était de :  $score
                """.trimIndent()
                txtHome.setText(fulltext)
            }
            editName.setText(name)
            editName.setSelection(name.length)
        }
        btnPlay.setEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_main_search ->{
                Toast.makeText(this, "Recherche indisponible, demandez plutôt l'avis de Google, c'est mieux et plus rapide.", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.menu_main_params ->  {
                Toast.makeText(this, "Il n'y a rien à paramétrer ici, passez votre chemin...", Toast.LENGTH_LONG).show();
                return true;
            }
            else-> return super.onOptionsItemSelected(item);
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_activity_main,menu)
        return true
    }
    fun configureToolBar(){
        setSupportActionBar(toolbar)
    }
    fun configureImageView(){
        imageOSS.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                launchDetailActivity()
            }
        })
    }
    fun launchDetailActivity(){
        val _Intent  = Intent(this@MainActivity, DetailActivity::class.java)
        this.startActivity(_Intent)
    }
    override fun onStart() {
        super.onStart()
        ChargeUser()
    }
}