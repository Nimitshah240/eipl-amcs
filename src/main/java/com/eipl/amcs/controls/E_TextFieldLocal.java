package com.eipl.amcs.controls;

import com.eipl.amcs.MainApp;
import com.ibm.icu.text.Transliterator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class E_TextFieldLocal extends TextField {


    private final Transliterator transliterator;
    private final StringBuilder currentWord = new StringBuilder();

    public E_TextFieldLocal() {

        transliterator = createTransliterator();


        addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.BACK_SPACE) {
                event.consume();
                handleBackspace();
            }
        });

        addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
            String ch = event.getCharacter();

            if (ch.isEmpty() || ch.charAt(0) < 32) return;

            event.consume();

            if (ch.equals(" ")) {
                flushCurrentWord();
                appendText(" ");
            } else {
                currentWord.append(ch);

                String existing = getText();
                if (existing.length() >= getPreviousGujaratiLength()) {
                    setText(existing.substring(0, existing.length() - getPreviousGujaratiLength()));
                }
                String preview = transliterator.transliterate(preprocess(currentWord.toString()));
                appendText(preview);
                previousGujaratiLength = preview.length();
                positionCaret(getText().length());
            }
        });
    }

    private int previousGujaratiLength = 0;

    private int getPreviousGujaratiLength() {
        return previousGujaratiLength;
    }

    private void flushCurrentWord() {
        if (currentWord.length() > 0) {
            String existing = getText();
            if (existing.length() >= previousGujaratiLength) {
                setText(existing.substring(0, existing.length() - previousGujaratiLength));
            }
            String finalWord = transliterator.transliterate(preprocess(currentWord.toString()));
            appendText(finalWord);
            positionCaret(getText().length());
            currentWord.setLength(0);
            previousGujaratiLength = 0;
        }
    }

    private void handleBackspace() {
        if (currentWord.length() > 0) {
            currentWord.deleteCharAt(currentWord.length() - 1);

            String existing = getText();
            if (existing.length() >= previousGujaratiLength) {
                setText(existing.substring(0, existing.length() - previousGujaratiLength));
            }

            if (currentWord.length() > 0) {
                String preview = transliterator.transliterate(preprocess(currentWord.toString()));
                appendText(preview);
                previousGujaratiLength = preview.length();
            } else {
                previousGujaratiLength = 0;
            }
            positionCaret(getText().length());
        } else {
            if (getText().length() > 0) {
                setText(getText().substring(0, getText().length() - 1));
                positionCaret(getText().length());
            }
        }
    }

    private String preprocess(String text) {
        return text
                .replace("aa", "ā")   // આ
                .replace("ii", "ī")   // ઈ
                .replace("ee", "ī")   // ઈ
                .replace("uu", "ū")   // ઊ
                .replace("oo", "ū")   // ઊ
                .replace("tt", "ṭ")   // ટ
                .replace("dd", "ḍ")   // ડ
                .replace("nn", "ṇ")   // ણ
                .replace("ll", "ḷ")   // ળ
                .replace("sh", "ś")   // શ
                .replace("shh", "ṣ")   // ષ
                .replace("chh", "ch")  // છ
                .replace("rr", "ṛ");  // ઋ
    }

    private Transliterator createTransliterator() {

        String language = MainApp.getLocale();

        String targetScript;

        switch (language) {
            case "hi":
            case "mr":
            case "ne":
                targetScript = "Devanagari";
                break;

            case "gu":
                targetScript = "Gujarati";
                break;

            case "bn":
                targetScript = "Bengali";
                break;

            case "ta":
                targetScript = "Tamil";
                break;

            case "te":
                targetScript = "Telugu";
                break;

            case "kn":
                targetScript = "Kannada";
                break;

            case "ml":
                targetScript = "Malayalam";
                break;

            case "pa":
                targetScript = "Gurmukhi";
                break;

            case "or":
                targetScript = "Oriya";
                break;

            default:
                targetScript = "Gujarati";
        }

        return Transliterator.getInstance("Latin-" + targetScript);
    }
}