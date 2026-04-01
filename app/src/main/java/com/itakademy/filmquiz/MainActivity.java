package com.itakademy.filmquiz;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private CardView cardFilm;
    private TextView tvQuestion;
    private TextView tvResultat;
    private ImageButton bIsFaux;
    private ImageButton bRestart;
    private ImageButton bIsVrai;
    private ArrayList<QuestionFilm> questions;
    private int indexQuestion = 0;
    private int score = 0;
    private float xDepart;
    private float yDepart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cardFilm = findViewById(R.id.cardFilm);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvResultat = findViewById(R.id.tvResultat);

        bIsFaux = findViewById(R.id.bIsFaux);
        bRestart = findViewById(R.id.bRestart);
        bIsVrai = findViewById(R.id.bIsVrai);

        initialiserQuestions();
        afficherQuestion();

        bIsVrai.setOnClickListener(v -> traiterReponse(true));
        bIsFaux.setOnClickListener(v -> traiterReponse(false));
        bRestart.setOnClickListener(v -> recommencerQuiz());

        activerSwipeCarte();
    }

    private void initialiserQuestions() {
        questions = new ArrayList<>();

        questions.add(new QuestionFilm("Titanic est un film d'horreur.", false));
        questions.add(new QuestionFilm("Inception parle des rêves.", true));
        questions.add(new QuestionFilm("Joker est une comédie romantique.", false));
        questions.add(new QuestionFilm("Interstellar est un film de science-fiction.", true));
        questions.add(new QuestionFilm("Le Parrain est un film sur la mafia.", true));
    }

    private void afficherQuestion() {
        if (indexQuestion < questions.size()) {
            QuestionFilm questionActuelle = questions.get(indexQuestion);
            tvQuestion.setText(questionActuelle.getTexteQuestion());

            cardFilm.setTranslationX(0f);
            cardFilm.setTranslationY(0f);
            cardFilm.setRotation(0f);
            cardFilm.setAlpha(1f);

            tvResultat.setText("");
        } else {
            tvQuestion.setText("Quiz terminé !");
            tvResultat.setText("Score : " + score + " / " + questions.size());
        }
    }

    private void traiterReponse(boolean reponseUtilisateur) {
        if (indexQuestion >= questions.size()) {
            return;
        }

        QuestionFilm questionActuelle = questions.get(indexQuestion);
        boolean bonneReponse = questionActuelle.isBonneReponse();
        boolean estCorrect = (reponseUtilisateur == bonneReponse);

        if (estCorrect) {
            score++;
            tvResultat.setText("Bonne réponse");
        } else {
            tvResultat.setText("Mauvaise réponse");
        }

        if (reponseUtilisateur) {
            animerCarteEtPasserQuestion(1000f, 25f);
        } else {
            animerCarteEtPasserQuestion(-1000f, -25f);
        }
    }

    private void animerCarteEtPasserQuestion(float translationX, float rotation) {
        cardFilm.animate()
                .translationX(translationX)
                .rotation(rotation)
                .alpha(0f)
                .setDuration(250)
                .withEndAction(() -> {
                    indexQuestion++;
                    afficherQuestion();
                })
                .start();
    }

    private void recommencerQuiz() {
        indexQuestion = 0;
        score = 0;
        tvResultat.setText("");
        afficherQuestion();
    }

    private void activerSwipeCarte() {
        cardFilm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xDepart = event.getRawX();
                        yDepart = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float deltaX = event.getRawX() - xDepart;
                        float deltaY = event.getRawY() - yDepart;

                        cardFilm.setTranslationX(deltaX);
                        cardFilm.setTranslationY(deltaY);
                        cardFilm.setRotation(deltaX / 20f);
                        return true;

                    case MotionEvent.ACTION_UP:
                        float distanceX = event.getRawX() - xDepart;

                        if (distanceX > 250) {
                            traiterReponse(true);
                        } else if (distanceX < -250) {
                            traiterReponse(false);
                        } else {
                            cardFilm.animate()
                                    .translationX(0f)
                                    .translationY(0f)
                                    .rotation(0f)
                                    .setDuration(200)
                                    .start();
                        }
                        return true;
                }

                return false;
            }
        });
    }
}
