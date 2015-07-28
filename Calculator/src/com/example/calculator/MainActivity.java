package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigDecimal;

public class MainActivity extends Activity {

    private EditText mShowResultEdt;
    private static final int DEF_DIV_SCALE = 10;
    private StringBuffer mNumberBufStr = null;
    private StringBuffer mShowResultBufStr = null;
    private Character mOperator = '=';
    private BigDecimal mPreOperand = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowResultEdt = (EditText) findViewById(R.id.result_id);
        getActionBar().hide();
        
        mNumberBufStr = new StringBuffer();
        mShowResultBufStr = new StringBuffer();
        mPreOperand = new BigDecimal(0);
    }

    /**
     * Button OnClickListener do number button click logic
     */
    public void doNumberBtnClicked(View v) {
        Button btn = (Button) v;
        Character numberChar = btn.getText().charAt(0);
        // after Equal calculate then enter number action
        if (mNumberBufStr.length() == 0 && mShowResultBufStr.length() > 0 && mOperator == '=') {
            mShowResultBufStr.delete(0, mShowResultBufStr.length());
        }
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
        }
        mNumberBufStr.append(numberChar);
        mShowResultBufStr.append(mNumberBufStr.toString());
        mShowResultEdt.setText(mShowResultBufStr.toString());
    }

    /**
     * calculator
     */
    private BigDecimal calculator(BigDecimal preOperands, Character operator, BigDecimal postOperands) {
        BigDecimal ans = new BigDecimal(0);
        if (operator == '+') {
            ans = preOperands.add(postOperands);
        } else if (operator == '-') {
            ans = preOperands.subtract(postOperands);
        } else if (operator == '*') {
            ans = preOperands.multiply(postOperands);
        } else if (operator == '/') {
            ans = preOperands.divide(postOperands, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        }
        String ansStr = ans.toString();
        while (ansStr.endsWith("0") || ansStr.endsWith(".")) {
            ansStr = ansStr.substring(0, ansStr.length() - 1);
        }
        return new BigDecimal(ansStr);
    }

    /**
     * Button OnClickListener do Operator button click logic
     */
    public void doOperatorBtnClicked(View v) {
        Button btn = (Button) v;
        if (mNumberBufStr.length() != 0) {
            if (mOperator != '=') {
                BigDecimal postOperand = new BigDecimal(mNumberBufStr.toString());
                if (mOperator == '/' && postOperand.toString().equals("0")) {
                    return;
                }
                mPreOperand = calculator(mPreOperand, mOperator, postOperand);
            } else {
                mPreOperand = new BigDecimal(mNumberBufStr.toString());
            }
            mOperator = btn.getText().charAt(0);
            mNumberBufStr.delete(0, mNumberBufStr.length());
            mShowResultBufStr.delete(0, mShowResultBufStr.length());
            mShowResultBufStr.append(mPreOperand.toString());
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
                BigDecimal postOperand = new BigDecimal(mNumberBufStr.toString());
                // handle divided by 0 problem
                if (mOperator == '/' && postOperand.doubleValue() == 0.0) {
                    return;
                } else {
                    mPreOperand = calculator(mPreOperand, mOperator, postOperand);
                }
            } else {
                mPreOperand = new BigDecimal(mNumberBufStr.toString());
            }

            mNumberBufStr.delete(0, mNumberBufStr.length());
            mShowResultBufStr.delete(0, mShowResultBufStr.length());
            mShowResultBufStr.append(mPreOperand.toString());
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
        mPreOperand = new BigDecimal(0);
    }

    /**
     * Button OnClickListener do backspace button click logic
     */
    public void doBackspaceBtnClicked(View v) {
        if (mShowResultBufStr.length() > 0) {
            Character lastChar = mShowResultBufStr.toString().charAt(mShowResultBufStr.length() - 1);
            // Judge last Character is number or operator
            if (mOperator == lastChar) {
                mOperator = '=';
            } else {
                if (mNumberBufStr.length() == 0) {
                    // after Equal calculate then enter Backspace action
                    if (mPreOperand.toString().equals(mShowResultBufStr.toString())) {
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