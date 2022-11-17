/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.manish.creditcardscanner.mlkit.textdetector;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static java.lang.Math.max;
import static java.lang.Math.min;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.Text.Line;
import com.google.mlkit.vision.text.Text.TextBlock;
import com.manish.creditcardscanner.MainActivity;
import com.manish.creditcardscanner.mlkit.GraphicOverlay;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextGraphic extends GraphicOverlay.Graphic {

  private static final String TAG = "TextGraphic";
  private static final String TEXT_WITH_LANGUAGE_TAG_FORMAT = "%s:%s";

  private static final int TEXT_COLOR = Color.WHITE;
  private static final int MARKER_COLOR = Color.WHITE;
  private static final float TEXT_SIZE = 54.0f;
  private static final float STROKE_WIDTH = 4.0f;

  private final Paint rectPaint;
  private final Paint textPaint;
  private final Paint labelPaint;
  private final Text text;
  private final boolean shouldGroupTextInBlocks;
  private final boolean showLanguageTag;
  private final boolean showConfidence;

  TextGraphic(
      GraphicOverlay overlay,
      Text text,
      boolean shouldGroupTextInBlocks,
      boolean showLanguageTag,
      boolean showConfidence) {
    super(overlay);

    this.text = text;
    this.shouldGroupTextInBlocks = shouldGroupTextInBlocks;
    this.showLanguageTag = showLanguageTag;
    this.showConfidence = showConfidence;

    rectPaint = new Paint();
    rectPaint.setColor(MARKER_COLOR);
    rectPaint.setStyle(Paint.Style.STROKE);
    rectPaint.setStrokeWidth(STROKE_WIDTH);

    textPaint = new Paint();
    textPaint.setColor(TEXT_COLOR);
    textPaint.setTextSize(TEXT_SIZE);

    labelPaint = new Paint();
    labelPaint.setColor(MARKER_COLOR);
    labelPaint.setStyle(Paint.Style.FILL);
    postInvalidate();
  }

  /** Draws the text block annotations for position, size, and raw value on the supplied canvas. */
  @Override
  public void draw(Canvas canvas) {
    Log.d(TAG, "Text is: " + text.getText());
    for (TextBlock textBlock : text.getTextBlocks()) {
      // Renders the text at the bottom of the box.
      Log.d(TAG, "TextBlock text is: " + textBlock.getText());
      Log.d(TAG, "TextBlock boundingbox is: " + textBlock.getBoundingBox());
      Log.d(TAG, "TextBlock cornerpoint is: " + Arrays.toString(textBlock.getCornerPoints()));
      if (shouldGroupTextInBlocks) {
        String text =
            showLanguageTag
                ? String.format(
                    TEXT_WITH_LANGUAGE_TAG_FORMAT,
                    textBlock.getRecognizedLanguage(),
                    textBlock.getText())
                : textBlock.getText();
        drawAR(
            text,
            new RectF(textBlock.getBoundingBox()),
            TEXT_SIZE * textBlock.getLines().size() + 2 * STROKE_WIDTH,
            canvas);
      } else {
        for (Line line : textBlock.getLines()) {
          Log.d(TAG, "Line text is: " + line.getText());
          Log.d(TAG, "Line boundingbox is: " + line.getBoundingBox());
          Log.d(TAG, "Line cornerpoint is: " + Arrays.toString(line.getCornerPoints()));
          Log.d(TAG, "Line confidence is: " + line.getConfidence());
          Log.d(TAG, "Line angle is: " + line.getAngle());
          String text =
              showLanguageTag
                  ? String.format(
                      TEXT_WITH_LANGUAGE_TAG_FORMAT, line.getRecognizedLanguage(), line.getText())
                  : line.getText();
          text =
              showConfidence
                  ? String.format(Locale.US, "%s (%.2f)", text, line.getConfidence())
                  : text;

          if(isRegexPatternMatch(text)){
              drawAR(text, new RectF(line.getBoundingBox()), TEXT_SIZE + 2 * STROKE_WIDTH, canvas);

              Log.i("CARDNO", text);
              if(false) {
                navigateToSnapshotNotFoundAxtivity();
              }
          }

          for (Text.Element element : line.getElements()) {
            Log.d(TAG, "Element text is: " + element.getText());
            Log.d(TAG, "Element boundingbox is: " + element.getBoundingBox());
            Log.d(TAG, "Element cornerpoint is: " + Arrays.toString(element.getCornerPoints()));
            Log.d(TAG, "Element language is: " + element.getRecognizedLanguage());
            Log.d(TAG, "Element confidence is: " + element.getConfidence());
            Log.d(TAG, "Element angle is: " + element.getAngle());
            for (Text.Symbol symbol : element.getSymbols()) {
              Log.d(TAG, "Symbol text is: " + symbol.getText());
              Log.d(TAG, "Symbol boundingbox is: " + symbol.getBoundingBox());
              Log.d(TAG, "Symbol cornerpoint is: " + Arrays.toString(symbol.getCornerPoints()));
              Log.d(TAG, "Symbol confidence is: " + symbol.getConfidence());
              Log.d(TAG, "Symbol angle is: " + symbol.getAngle());
            }
          }
        }
      }
    }
  }

  private void navigateToSnapshotNotFoundAxtivity() {
    Class cls = null;
    try {
      cls = Class.forName("com.manish.creditcardscanner.SnapshotNotFoundActivity");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    Intent intent = new Intent(this.getApplicationContext(), cls);
    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
    this.getApplicationContext().startActivity(intent);
  }

  private void drawAR(String text, RectF rect, float textHeight, Canvas canvas) {
    float x0 = translateX(rect.left);
    float x1 = translateX(rect.right);
    rect.left = min(x0, x1) - 80;
    rect.right = max(x0, x1) + 80;
    rect.top = translateY(rect.top - 160);
    rect.bottom = translateY(rect.bottom + 110);
//    canvas.drawRect(rect, rectPaint); // Card Box
//    float textWidth = textPaint.measureText(text);
//    canvas.drawRect(
//        rect.left - STROKE_WIDTH,
//        rect.top - textHeight - 200,
//        rect.left + textWidth + 220,
//        rect.top,
//        labelPaint);
//    Renders the text at the bottom of the box.
//    canvas.drawText(text, rect.left, rect.top - STROKE_WIDTH, textPaint); // Text Box

//    Bitmap piechart = BitmapFactory.decodeResource(
//                      this.getApplicationContext().getResources(),
//                      R.drawable.pie);
    Bitmap piechart = MainActivity.loadPieChartBitmapFromView();
    Bitmap activity = MainActivity.loadBitmapFromView();
    Log.i("TextGraphic", "Recent Activity Display Success");
    canvas.drawBitmap(
            piechart,
            null,
            new RectF(rect.left, rect.top - 520, rect.right, rect.top - 10),
            null);
    canvas.drawBitmap(
            activity,
            null,
            new RectF(rect.left, rect.bottom, rect.right, rect.bottom + 520),
            null);
  }

  private boolean isRegexPatternMatch(String input) {
    Pattern p = Pattern.compile(
            "^\\d\\d\\d\\d\\s\\d\\d\\d\\d\\s\\d\\d\\d\\d\\s\\d\\d\\d\\d$",
            Pattern.CASE_INSENSITIVE);
    input = refactorCardNo(input);
    Matcher m = p.matcher(input);
    if (m.matches()) {
      Log.i("CREDITCARDREGEX", "regex match found");
      return true;
    }
    return false;
  }

  @NonNull
  private String refactorCardNo(String input) {
    String temp = input;
    temp = temp.replace("b", "6");
    temp = temp.replace("B", "6");
    temp = temp.replace("s", "5");
    temp = temp.replace("S", "5");
    temp = temp.replace("i", "1");
    temp = temp.replace("I", "1");
    temp = temp.replace("T", "7");
    temp = temp.replace("t", "7");
//    temp = temp.replace(" ", "");
//    Log.i("CREDITCARDINPUT", temp);
    return temp;
  }
}
