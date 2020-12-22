/*
 * Copyright 2014 Evgeny Shishkin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.test.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test.R;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Utilities for working with roboto typefaces.
 *
 * @author Evgeny Shishkin
 */
public final class RobotoTypefaces {

    @Retention(SOURCE)
    @IntDef({
            TYPEFACE_ROBOTO_LIGHT,
            TYPEFACE_ROBOTO_REGULAR,
            TYPEFACE_ROBOTO_MEDIUM,
            TYPEFACE_ROBOTO_BOLD
    })
    public @interface RobotoTypeface {
    }

    public static final int TYPEFACE_ROBOTO_LIGHT = 0;
    public static final int TYPEFACE_ROBOTO_REGULAR = 1;
    public static final int TYPEFACE_ROBOTO_MEDIUM = 2;
    public static final int TYPEFACE_ROBOTO_BOLD = 3;

    @Retention(SOURCE)
    @IntDef({
            FONT_FAMILY_ROBOTO
    })
    public @interface RobotoFontFamily {
    }

    public static final int FONT_FAMILY_ROBOTO = 0;

    @Retention(SOURCE)
    @IntDef({
            TEXT_WEIGHT_NORMAL,
            TEXT_WEIGHT_LIGHT,
            TEXT_WEIGHT_MEDIUM,
            TEXT_WEIGHT_BOLD
    })
    public @interface RobotoTextWeight {
    }

    public static final int TEXT_WEIGHT_NORMAL = 0;
    public static final int TEXT_WEIGHT_LIGHT = 1;
    public static final int TEXT_WEIGHT_MEDIUM = 2;
    public static final int TEXT_WEIGHT_BOLD = 3;


    @Retention(SOURCE)
    @IntDef({
            TEXT_STYLE_NORMAL
    })
    public @interface RobotoTextStyle {
    }

    public static final int TEXT_STYLE_NORMAL = 0;

    /**
     * Array of created typefaces for later reused.
     */
    private static final SparseArray<Typeface> typefacesCache = new SparseArray<>(32);

    private RobotoTypefaces() {
    }

    /**
     * Obtain typeface.
     *
     * @param context       The Context the widget is running in, through which it can access the current theme, resources, etc.
     * @param typefaceValue The value of "robotoTypeface" attribute
     * @return specify {@link Typeface} or throws IllegalArgumentException if unknown `robotoTypeface` attribute value.
     */
    @NonNull
    public static Typeface obtainTypeface(@NonNull Context context, @RobotoTypeface int typefaceValue) {
        Typeface typeface = typefacesCache.get(typefaceValue);
        if (typeface == null) {
            typeface = createTypeface(context, typefaceValue);
            typefacesCache.put(typefaceValue, typeface);
        }
        return typeface;
    }

    /**
     * Obtain typeface.
     *
     * @param context    The Context the widget is running in, through which it can access the current theme, resources, etc.
     * @param fontFamily The value of "robotoFontFamily" attribute
     * @param textWeight The value of "robotoTextWeight" attribute
     * @param textStyle  The value of "robotoTextStyle" attribute
     * @return specify {@link Typeface} or throws IllegalArgumentException.
     */
    @NonNull
    public static Typeface obtainTypeface(@NonNull Context context, @RobotoFontFamily int fontFamily,
                                          @RobotoTextWeight int textWeight, @RobotoTextStyle int textStyle) {
        @RobotoTypeface int typeface;
        if (fontFamily == FONT_FAMILY_ROBOTO) {
            if (textStyle == TEXT_STYLE_NORMAL) {
                switch (textWeight) {
                    case TEXT_WEIGHT_NORMAL:
                        typeface = TYPEFACE_ROBOTO_REGULAR;
                        break;
                    case TEXT_WEIGHT_LIGHT:
                        typeface = TYPEFACE_ROBOTO_LIGHT;
                        break;
                    case TEXT_WEIGHT_MEDIUM:
                        typeface = TYPEFACE_ROBOTO_MEDIUM;
                        break;
                    case TEXT_WEIGHT_BOLD:
                        typeface = TYPEFACE_ROBOTO_BOLD;
                        break;
                    default:
                        throw new IllegalArgumentException("`robotoTextWeight` attribute value " + textWeight +
                                " is not supported for this fontFamily " + fontFamily +
                                " and textStyle " + textStyle);
                }
            } else {
                throw new IllegalArgumentException("`robotoTextStyle` attribute value " + textStyle +
                        " is not supported for this fontFamily " + fontFamily);
            }
        } else {
            throw new IllegalArgumentException("Unknown `robotoFontFamily` attribute value " + fontFamily);
        }
        return obtainTypeface(context, typeface);
    }

    /**
     * Create typeface from assets.
     *
     * @param context  The Context the widget is running in, through which it can
     *                 access the current theme, resources, etc.
     * @param typeface The value of "robotoTypeface" attribute
     * @return Roboto {@link Typeface} or throws IllegalArgumentException if unknown `robotoTypeface` attribute value.
     */
    @NonNull
    private static Typeface createTypeface(@NonNull Context context, @RobotoTypeface int typeface) {
        String path;
        switch (typeface) {
            case TYPEFACE_ROBOTO_LIGHT:
                path = "fonts/Roboto-Light.ttf";
                break;
            case TYPEFACE_ROBOTO_REGULAR:
                path = "fonts/Roboto-Regular.ttf";
                break;
            case TYPEFACE_ROBOTO_MEDIUM:
                path = "fonts/Roboto-Medium.ttf";
                break;
            case TYPEFACE_ROBOTO_BOLD:
                path = "fonts/Roboto-Bold.ttf";
                break;
            default:
                throw new IllegalArgumentException("Unknown `robotoTypeface` attribute value " + typeface);
        }
        return Typeface.createFromAsset(context.getAssets(), path);
    }

    /**
     * Obtain typeface from attributes.
     *
     * @param context The Context the widget is running in, through which it can access the current theme, resources, etc.
     * @param attrs   The styled attribute values in this Context's theme.
     * @return specify {@link Typeface}
     */
    @NonNull
    public static Typeface obtainTypeface(@NonNull Context context, @NonNull TypedArray attrs) {
        if (attrs.hasValue(R.styleable.RobotoTextView_robotoTypeface)) {
            @RobotoTypeface int typefaceValue = attrs.getInt(R.styleable.RobotoTextView_robotoTypeface, TYPEFACE_ROBOTO_REGULAR);
            return obtainTypeface(context, typefaceValue);
        } else {
            @RobotoFontFamily int fontFamily = attrs.getInt(R.styleable.RobotoTextView_robotoFontFamily, FONT_FAMILY_ROBOTO);
            @RobotoTextWeight int textWeight = attrs.getInt(R.styleable.RobotoTextView_robotoTextWeight, TEXT_WEIGHT_NORMAL);
            @RobotoTextStyle int textStyle = TEXT_STYLE_NORMAL;
            return obtainTypeface(context, fontFamily, textWeight, textStyle);
        }
    }

    /**
     * Set up typeface for TextView from the attributes.
     *
     * @param textView The roboto text view
     * @param context  The context the widget is running in, through which it can
     *                 access the current theme, resources, etc.
     * @param attrs    The attributes of the XML tag that is inflating the widget.
     */
    public static void setUpTypeface(@NonNull TextView textView, @NonNull Context context, @Nullable AttributeSet attrs) {
        Typeface typeface;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RobotoTextView);
            try {
                typeface = obtainTypeface(context, a);
            } finally {
                a.recycle();
            }
        } else {
            typeface = obtainTypeface(context, TYPEFACE_ROBOTO_REGULAR);
        }
        setUpTypeface(textView, typeface);
    }

    /**
     * Set up typeface for TextView.
     *
     * @param textView The text view
     * @param typeface The value of "robotoTypeface" attribute
     */
    public static void setUpTypeface(@NonNull TextView textView, @RobotoTypeface int typeface) {
        setUpTypeface(textView, obtainTypeface(textView.getContext(), typeface));
    }

    /**
     * Set up typeface for TextView.
     *
     * @param textView   The text view
     * @param fontFamily The value of "robotoFontFamily" attribute
     * @param textWeight The value of "robotoTextWeight" attribute
     * @param textStyle  The value of "robotoTextStyle" attribute
     */
    public static void setUpTypeface(@NonNull TextView textView, @RobotoFontFamily int fontFamily,
                                     @RobotoTextWeight int textWeight, @RobotoTextStyle int textStyle) {
        setUpTypeface(textView, obtainTypeface(textView.getContext(), fontFamily, textWeight, textStyle));
    }

    /**
     * Set up typeface for TextView. Wrapper over {@link TextView#setTypeface(Typeface)}
     * for making the font anti-aliased.
     *
     * @param textView The text view
     * @param typeface The specify typeface
     */
    public static void setUpTypeface(@NonNull TextView textView, @NonNull Typeface typeface) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        textView.setTypeface(typeface);
    }
}