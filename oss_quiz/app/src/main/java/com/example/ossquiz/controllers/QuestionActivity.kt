package com.example.ossquiz.controllers

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ossquiz.R
import com.example.ossquiz.models.BoiteAQuestion
import com.example.ossquiz.models.Question
import kotlinx.android.synthetic.main.activity_question.*
import java.util.*

@Suppress("DEPRECATION")
class QuestionActivity : AppCompatActivity(), View.OnClickListener  {
    lateinit var _MaBoiteAQuestions : BoiteAQuestion
    lateinit var _MaQuestionActuelle : Question
    var _NbQuestions : Int = 0
    var _Score : Int = 0
    var _NbBlanquette : Int = 0
    var _OSS : Boolean = false
    var _ClickedEventsEnabled : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        var intent = intent
        _OSS = intent.getBooleanExtra("User",true)

        _MaBoiteAQuestions = GenereMaBoiteAQuestion()
        _NbQuestions = _MaBoiteAQuestions._LesQuestions.size

        btnVrai.tag = 0
        btnVrai.setOnClickListener(this)

        btnFaux.tag = 1
        btnFaux.setOnClickListener(this)

        if (_OSS){
            btnOSS.tag =2
            btnOSS.visibility=View.VISIBLE
            btnOSS.setOnClickListener(this)
        }

        _MaQuestionActuelle = _MaBoiteAQuestions.GetQuestion()
        this.SetQuestionActuelle(_MaQuestionActuelle)
    }

    private fun GenereMaBoiteAQuestion(): BoiteAQuestion {
        var _Question1 = Question("J'aime me battre ?",true)
        var _Question2 = Question("Les chinois de chine sont alliés aux nazis ?",false)
        var _Question3 = Question("T'es mauvais Jack ?",true)

        return BoiteAQuestion(Arrays.asList(_Question1,
                                            _Question2,
                                            _Question3))
    }

    private fun SetQuestionActuelle(question :Question){
        txtQuestion.setText(question._Question)
    }

    override fun onClick(v: View?) {
        var indexReponse : Int = v?.tag as Int
        var reponseDonnee : Boolean = false

        if(indexReponse==0)
            reponseDonnee =true;

        if( indexReponse == 2)
        {
            Toast.makeText(this,"La blanquette est bonne", Toast.LENGTH_SHORT).show()
            _NbBlanquette++
        }

        else
             if (reponseDonnee == _MaQuestionActuelle._Reponse)
             {
                 Toast.makeText(this,"Bonne réponse", Toast.LENGTH_SHORT).show()
                 _Score++
             }
             else
                 Toast.makeText(this,"Mauvaise réponse", Toast.LENGTH_SHORT).show()

        _ClickedEventsEnabled = false

        Handler().postDelayed(object :Runnable{
            override fun run() {

                _ClickedEventsEnabled = true

                if(--_NbQuestions==0){
                    FinDuJeu()
                } else{
                    _MaQuestionActuelle = _MaBoiteAQuestions.GetQuestion()
                    SetQuestionActuelle(_MaQuestionActuelle)
                }
                //On met un temps d'attente pour lire le message toast avant de mettre la nouvelle question (s'il y en a une)
            }
        },2000)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return _ClickedEventsEnabled && super.dispatchTouchEvent(ev)
    }

    private fun FinDuJeu(){
        var builder = AlertDialog.Builder(this)
        val _IntentMainActivity  = Intent()
        if (_OSS){
            builder.setTitle("Beau travail OSS 117")
                .setMessage("Vous avez lutter pour la gloire de la SCEP"+
                        "\nQuand on est OSS 117 toutes nos missions sont un succès"+
                        "\nAppuyez sur OK pour votre nouvelle mission qui ce déroulera le 3 février 2021")
                .setPositiveButton("OK", object :  DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        setResult(Activity.RESULT_OK,_IntentMainActivity)
                        finish()
                    }
                })
                .create()
                .show()
        }
        else{
            builder.setTitle("Fin du jeu")
                .setMessage("Votre score est de : "+_Score +"\nAppuyez sur OK pour quitter")
                .setPositiveButton("OK", object :  DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val _IntentMainActivity  = Intent()
                        _IntentMainActivity.putExtra(Constants.KEY_SCORES, _Score)
                        setResult(Activity.RESULT_OK,_IntentMainActivity)
                        finish()
                    }
                })
                .create()
                .show()
        }
    }
}