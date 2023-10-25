package com.example.zad1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "com.example.zad1.correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;
    private String TAG = "MainActivity";
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private TextView questionTextView;
    private boolean answerWasShown;
    private Question[] questions = new Question[] {
            new Question(R.string.q1, false),
            new Question(R.string.q2, true),
            new Question(R.string.q3, true),
            new Question(R.string.q4, false),
            new Question(R.string.q5, true),
            new Question(R.string.q6, false)
    };
    private int currentIndex = 0, correctAnswers = 0, incorrectAnswers = 0;
    private boolean answered = false;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Wywołano onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Wywołano onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Wywołano onDestory()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Wywołano onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Wywołano onResume()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Wywołana została metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) { return; }
        if (requestCode == REQUEST_CODE_PROMPT) {
            if (data == null) { return; }
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Wywołano onCreate()");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);
        promptButton = findViewById(R.id.prompt_button);

        setNextQuestion();
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answered) {
                    answered();
                } else {
                    checkAnswer(true);
                }
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answered) {
                    answered();
                } else {
                    checkAnswer(false);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % questions.length;
                answerWasShown = false;
                answered = false;
                setNextQuestion();
            }
        });

        promptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].getTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
                startActivityForResult(intent, REQUEST_CODE_PROMPT);
            }
        });
    }

    private void setNextQuestion() {
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    private void checkAnswer(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].getTrueAnswer();
        int resultMessageId = 0;
        if (answerWasShown) {
            resultMessageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                resultMessageId = R.string.correct_answer;
                correctAnswers++;
            } else {
                resultMessageId = R.string.incorrect_answer;
                incorrectAnswers++;
            }
            answered = true;
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
        if (currentIndex == 5) {
            Toast.makeText(this, "Liczba poprawnych odpowiedzi:" + correctAnswers, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Liczba niepoprawnych odpowiedzi:" + incorrectAnswers, Toast.LENGTH_SHORT).show();
            correctAnswers = 0;
            incorrectAnswers = 0;
        }
    }

    private void answered() {
        Toast.makeText(this, R.string.answered, Toast.LENGTH_SHORT).show();
    }
}