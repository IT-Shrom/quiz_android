package com.itakademy.filmquiz;

public class QuestionFilm {

    private final String texteQuestion;
    private final boolean bonneReponse;

    public QuestionFilm(String texteQuestion, boolean bonneReponse) {
        this.texteQuestion = texteQuestion;
        this.bonneReponse = bonneReponse;
    }

    public String getTexteQuestion() {
        return texteQuestion;
    }

    public boolean isBonneReponse() {
        return bonneReponse;
    }
}
