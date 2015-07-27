package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {

    private EditText mShowResultEdt;
    // Define division scale
    private static final int DEF_DIV_SCALE = 10;
    // Define calculator support operator list
    private ArrayList<Character> mOperatorList = new ArrayList<Character>(Arrays.asList('+', '-', '*', '/'));
    private StringBuffer mNumberBufStr = null;
    private StringBuffer mShowResultBufStr = null;
    private Character mOperator = '=';
    private double mPreOperand = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowResultEdt = (EditText) findViewById(R.id.result_id);
        getActionBar().hide();
        // initial
        mNumberBufStr = new StringBuffer();
        mShowResultBufStr = new StringBuffer();
    }

    /**
     * Button OnClickListener do number button click logic
     */
    public void doNumberBtnClicked(View v) {
        Button btn = (Button) v;
        Character numberChar = btn.getText().charAt(0);
        // avoid '.' char cause double value analysis error.
        if (numberChar == '.') {
            if (mNumberBufStr.length() == 0 || mNumberBufStr.toString().indexOf('.') != -1) {
                return;
            }
        }
        // clear showResult String for mNumBufStr merge
        if (mNumberBufStr.length() != 0) {
            if (mShowResultBufStr.toString().endsWith(mNumberBufStr.toString()))
                mShowResultBufStr.delete(mShowResultBufStr.length() - mNumberBufStr.length(),
                        mShowResultBufStr.length());
        } else {
            // after Equal calculate then enter number action
            if (mOperator == '=')
                mShowResultBufStr.delete(0, mShowResultBufStr.length());
        }
        mNumberBufStr.append(numberChar);
        mShowResultBufStr.append(mNumberBufStr.toString());
        mShowResultEdt.setText(mShowResultBufStr.toString());
    }

    /**
     * calculator
     */
    private double calculator(double preOperands, Character operator, double postOperands) {
        BigDecimal bPreOperands = new BigDecimal(Double.toString(preOperands));
        BigDecimal bPostOperands = new BigDecimal(Double.toString(postOperands));
        BigDecimal ans = new BigDecimal("0");
        if (operator == '+') {
            ans = bPreOperands.add(bPostOperands);
        } else if (operator == '-') {
            ans = bPreOperands.subtract(bPostOperands);
        } else if (operator == '*') {
            ans = bPreOperands.multiply(bPostOperands);
        } else if (operator == '/') {
            ans = bPreOperands.divide(bPostOperands, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        }
        return ans.doubleValue();
    }

    /**
     * Button OnClickListener do Operator button click logic
     */
    public void doOperatorBtnClicked(View v) {
        Button btn = (Button) v;
        if (mNumberBufStr.length() != 0) {
            if (mOperator != '=') {
                mPreOperand = calculator(mPreOperand, mOperator, Double.valueOf(mNumberBufStr.toString()));
            } else {
                mPreOperand = Double.valueOf(mNumberBufStr.toString());
            }
            mOperator = btn.getText().charAt(0);
            mNumberBufStr.delete(0, mNumberBufStr.length());
            mShowResultBufStr.delete(0, mShowResultBufStr.length());
            mShowResultBufStr.append(String.valueOf(mPreOperand));
            if (mShowResultBufStr.toString().endsWith(".0")) {
                mShowResultBufStr.delete(mShowResultBufStr.length() - 2, mShowResultBufStr.length());
            }
            mShowResultBufStr.append(mOperator);
            mShowResultEdt.setText(mShowResultBufStr.toString());
        } else {
            if (mOperator != '=') {
                // for change operator
                if (mShowResultBufStr.toString().endsWith(mOperator.toString())) {
                    mOperator = btn.getText().charAt(0);
                    mShowResultBufStr.delete(mShowResultBufStr.length() - 1, mShowResultBufStr.length());
                    mShowResultBufStr.append(mOperator);
                    mShowResultEdt.setText(mShowResultBufStr.toString());
                }
            } else {
                // after Equal calculate
                if (mShowResultBufStr.length() != 0) {
                    mOperator = btn.getText().charAt(0);
                    mShowResultBufStr.append(mOperator);
                    mShowResultEdt.setText(mShowResultBufStr.toString());
                }
            }
        }
    }

    /**
     * Button OnClickListener do Operator button click logic
     */
    public void doEqualBtnClicked(View v) {
        if (mNumberBufStr.length() != 0) {
            if (mOperator != '=') {
                Double postOperand = Double.valueOf(mNumberBufStr.toString());
                // handle divided by 0 problem
                if (mOperator == '/' && postOperand == 0.0) {
                    return;
                } else {
                    mPreOperand = calculator(mPreOperand, mOperator, postOperand);
                }
            } else {
                mPreOperand = Double.valueOf(mNumberBufStr.toString());
            }

            mNumberBufStr.delete(0, mNumberBufStr.length());
            mShowResultBufStr.delete(0, mShowResultBufStr.length());
            mShowResultBufStr.append(String.valueOf(mPreOperand));
            if (mShowResultBufStr.toString().endsWith(".0")) {
                mShowResultBufStr.delete(mShowResultBufStr.length() - 2, mShowResultBufStr.length());
            }
            mShowResultEdt.setText(mShowResultBufStr.toString());

        }
        mOperator = '=';
    }

    /**
     * Button OnClickListener do clear button click logic
     */
    public void doClearBtnClicked(View v) {
        mNumberBufStr.delete(0, mNumberBufStr.length());
        mShowResultBufStr.delete(0, mShowResultBufStr.length());
        mShowResultEdt.setText(mShowResultBufStr.toString());
        mOperator = '=';
        mPreOperand = 0.0;
    }

    /**
     * Button OnClickListener do backspace button click logic
     */
    public void doBackspaceBtnClicked(View v) {
        if (mShowResultBufStr.length() > 0) {
            Character lastChar = mShowResultBufStr.toString().charAt(mShowResultBufStr.length() - 1);
            // Judge last Character is number or operator
            if (mOperatorList.contains(lastChar)) {
                mOperator = '=';
            } else {
                if (mNumberBufStr.length() == 0) {
                    // after Equal calculate then enter Backspace action
                    if (Double.valueOf(mShowResultBufStr.toString()) == mPreOperand) {
                        mNumberBufStr.append(mShowResultBufStr.toString());
                        mNumberBufStr.delete(mNumberBufStr.length() - 1, mNumberBufStr.length());
                    }
                } else {
                    mNumberBufStr.delete(mNumberBufStr.length() - 1, mNumberBufStr.length());
                }
            }
            mShowResultBufStr.delete(mShowResultBufStr.length() - 1, mShowResultBufStr.length());
            mShowResultEdt.setText(mShowResultBufStr.toString());
        }
    }
}