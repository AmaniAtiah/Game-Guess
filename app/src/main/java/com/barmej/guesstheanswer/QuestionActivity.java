package com.barmej.guesstheanswer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    private static final String TAG = QuestionActivity.class.getSimpleName();
    private static final String BUNDLE_CURRENT_INDEX = "BUNDLE_CURRENT_INDEX";
    private TextView mTextViewQuestion;
    private String[] QUESTIONS;
    Menu mmenuChangeLang;
    private int number = 1;
    private int number1 = 2;
    private int number3 = 3;
    private int number4 = 4;
    private int number5 = 5;
    private int number6 = 6;

    private int number7 = 7;
    private static final boolean[] ANSWERS = {
            false,
            true,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            true,
            false,
            true
    };

    private String[] ANSWERS_DETAILS;


    private String mCurrentQuestion,mCurrentAnswerDetail;
    private boolean mCurrentAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_PREF ,MODE_PRIVATE);
        String appLang = sharedPreferences.getString(Constants.APP_LANG,"");
        if(!appLang.equals(""))
            LocaleHelper.setLocale(this,appLang);


        setContentView(R.layout.activity_main);

        mmenuChangeLang = findViewById(R.id.menuChangeLang);
        mTextViewQuestion = findViewById(R.id.text_view_question);
        QUESTIONS = getResources().getStringArray(R.array.questions);
        ANSWERS_DETAILS = getResources().getStringArray(R.array.answers_details);
        showNewQuestion();



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuChangeLang) {
            showLanguageDialog();

            return true;


        } else {

            return super.onOptionsItemSelected(item);

        }
    }

    private void showLanguageDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.change_lang_text)
                .setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String language = "ar";
                        switch (which) {
                            case 0:
                                language = "ar";


                                break;
                            case 1:
                                language = "en";
                                break;
                            case 2:
                                language = "fr";
                                break;
                        }
                        saveLanguage(language);
                        LocaleHelper.setLocale(QuestionActivity.this, language);
                        Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        //reload();


                    }
                }).create();
        alertDialog.show();
    }

    private void reload() {
        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);

            startActivity(intent);
            overridePendingTransition(0, 0);

        }
    }


    private void saveLanguage(String lang) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_PREF ,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.APP_LANG,lang);

        editor.apply();


    }

    private void showNewQuestion() {
        Random random = new Random();
        int randomQuestionIndex = random.nextInt(QUESTIONS.length);
        mCurrentQuestion = QUESTIONS[randomQuestionIndex];
        mCurrentAnswer = ANSWERS[randomQuestionIndex];
        mCurrentAnswerDetail = ANSWERS_DETAILS[randomQuestionIndex];
        showQuestion();

    }
    public void onChangeQuestionClicked(View view) {
        showNewQuestion();
    }

    private void showQuestion() {
        mTextViewQuestion.setText(mCurrentQuestion);
    }


    public void onTrueClicked(View view) {
        if(mCurrentAnswer == true) {
            Toast.makeText(this,"الاجابة صحيحية",Toast.LENGTH_SHORT).show();
            showNewQuestion();
        }else {
            Toast.makeText(this,"الاجابة خاطئة",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QuestionActivity.this,AnswerActivity.class);
            intent.putExtra(Constants.QUESTION_ANSWER ,mCurrentAnswerDetail);
            startActivity(intent);


        }
    }
    public void onFalseClicked(View view) {
        if(mCurrentAnswer ==false){
            Toast.makeText(this,"الاجابة صحيحية",Toast.LENGTH_SHORT).show();
            showNewQuestion();
        }else {
            Toast.makeText(this,"الاجابة خاطئة",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QuestionActivity.this,AnswerActivity.class);
            intent.putExtra(Constants.QUESTION_ANSWER ,mCurrentAnswerDetail);
            startActivity(intent);

        }
    }

    public void onShareQuestionClicked(View view) {
        Intent intent = new Intent(QuestionActivity.this,ShareActivity.class);
        intent.putExtra(Constants.QUESTION_TEXT_EXTRA,mCurrentQuestion);

        startActivity(intent);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {

                Log.d(TAG, "display =" + mCurrentQuestion);
                mCurrentQuestion = savedInstanceState.getString("image");
                showQuestion();

        }
        Log.i(TAG,"onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("image", mCurrentQuestion);

        Log.i(TAG,"onSaveInstanceState");
    }


}

