package com.eipl.amcs.controls;

import com.eipl.amcs.MainApp;
import com.ibm.icu.text.Transliterator;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;


public class E_ComboBox<T> extends ComboBox<T> {

    private Transliterator transliterator;
    private StringBuilder currentWord = new StringBuilder();
    private int previousGujaratiLength = 0;

    public E_ComboBox() {

        if (MainApp.locale.equalsIgnoreCase("en")) {
            setOnKeyReleased(new FocusHandler());
            return;
        }
        setOnKeyReleased(new FocusHandler());


        setEditable(true);
        transliterator = createTransliterator();

        // ── Attach filters to the EDITOR directly, not the ComboBox ──
        // getEditor() is null until the skin is applied, so wait for it
        skinProperty().addListener((obs, oldSkin, newSkin) -> {
            TextField ed = getEditor();
            if (ed == null) return;

            ed.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.BACK_SPACE) {
                    event.consume();
                    handleBackspace();
                }
            });

            ed.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
                String ch = event.getCharacter();
                if (ch.isEmpty() || ch.charAt(0) < 32) return;

                event.consume();

                if (ch.equals(" ")) {
                    flushCurrentWord();
                    appendText(" ");
                } else {
                    currentWord.append(ch);

                    String existing = getText();
                    if (existing != null && existing.length() >= previousGujaratiLength) {
                        setText(existing.substring(0, existing.length() - previousGujaratiLength));
                    }
                    String preview = transliteratePreservingDigits(currentWord.toString());
                    appendText(preview);
                    previousGujaratiLength = preview.length();
                    positionCaret(getText().length());
                }
            });
        });
    }

    // ── Delegate text methods to the editor TextField ──────────────────────

    private TextField editor() {
        return getEditor();
    }

    private String getText() {
        return editor().getText();
    }

    private void setText(String text) {
        editor().setText(text);
    }

    private void appendText(String text) {
        editor().appendText(text);
    }

    private void positionCaret(int pos) {
        editor().positionCaret(pos);
    }

    // ── Rest of the logic ──────────────────────────────────────────────────

    private void flushCurrentWord() {
        if (currentWord.length() > 0) {
            String existing = getText();
            if (existing.length() >= previousGujaratiLength) {
                setText(existing.substring(0, existing.length() - previousGujaratiLength));
            }
            String finalWord = transliteratePreservingDigits(currentWord.toString());
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
            if (existing != null && existing.length() >= previousGujaratiLength) {
                setText(existing.substring(0, existing.length() - previousGujaratiLength));
            }

            if (currentWord.length() > 0) {
                String preview = transliteratePreservingDigits(currentWord.toString());
                appendText(preview);
                previousGujaratiLength = preview.length();
            } else {
                previousGujaratiLength = 0;
            }
            positionCaret(getText().length());
        } else {
            String text = getText();
            if (text != null && text.length() > 0) {
                setText(text.substring(0, text.length() - 1));
                positionCaret(getText().length());
            }
        }
    }

    private String preprocess(String text) {
        return text
                // ── Nasal conjuncts (must come BEFORE single consonants) ──
                .replace("nj", "ñj")   // ઞ્જ  → નજ conjunct
                .replace("nd", "nd")   // ન્દ  → keep for ICU but force conjunct
                .replace("nt", "nt")   // ન્ત
                .replace("nk", "ṅk")   // ઙ્ક
                .replace("ng", "ṅg")   // ઙ્ગ
                .replace("mb", "mb")   // મ્બ
                .replace("mp", "mp")   // મ્પ
                .replace("shh", "ṣ")
                .replace("sh", "ś")
                .replace("chh", "ch")
                .replace("aa", "ā")
                .replace("ii", "ī")
                .replace("ee", "ī")
                .replace("uu", "ū")
                .replace("oo", "ū")
                .replace("tt", "ṭ")
                .replace("dd", "ḍ")
                .replace("nn", "ṇ")
                .replace("ll", "ḷ")
                .replace("rr", "ṛ");
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

    public String getFinalText() {
        T selected = getValue();
        String editorText = getText();
        if (selected != null && selected.toString().equals(editorText)) {
            return selected.toString();
        }
        return editorText != null ? editorText : "";
    }

    private String transliteratePreservingDigits(String input) {
        String preprocessed = preprocess(input);

        // Split on digit boundaries, transliterate only non-digit segments
        StringBuilder result = new StringBuilder();
        StringBuilder segment = new StringBuilder();

        for (int i = 0; i < preprocessed.length(); i++) {
            char c = preprocessed.charAt(i);

            if (Character.isDigit(c)) {
                // Flush any pending non-digit segment
                if (segment.length() > 0) {
                    result.append(transliterator.transliterate(segment.toString()));
                    segment.setLength(0);
                }
                result.append(c); // Keep digit as-is (ASCII)
            } else {
                segment.append(c);
            }
        }

        // Flush remaining segment
        if (segment.length() > 0) {
            result.append(transliterator.transliterate(segment.toString()));
        }

        return result.toString();
    }
}

