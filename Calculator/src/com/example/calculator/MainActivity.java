package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    private EditText mShowResultEdt;

    private StringBuffer mNumberBufStr = null;
    private Character mOperator = null;
    private double mNum = 0.0;
    private double mPreOperands = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowResultEdt = (EditText) findViewById(R.id.result_id);

        // initial
        mNumberBufStr = new StringBuffer();
        doDataReset();
    }

    /**
     * Button OnClickListener do number button click logic
     */
    public void doNumberBtnClicked(View v) {
        Button btn = (Button) v;
        Character c = btn.getText().charAt(0);

        if (c == '.' && mNumberBufStr.length() == 0)
            return;

        mNumberBufStr.append(c);
        mNum = Double.valueOf(mNumberBufStr.toString()).doubleValue();
        mShowResultEdt.setText(mNumberBufStr);
    }

    /**
     * calculator
     */
    private double calculator(double preOperands, Character operator, double postOperands) {
        double ans = 0.0;
        if (operator == '+') {
            ans = preOperands + postOperands;
        } else if (operator == '-') {
            ans = preOperands - postOperands;
        } else if (operator == '*') {
            ans = preOperands * postOperands;
        } else if (operator == '/') {
            ans = preOperands / postOperands;
        }
        return ans;
    }

    /**
     * Button OnClickListener do Operator button click logic
     */
    public void doOperatorBtnClicked(View v) {
        if (mPreOperands != 0.0 && mPreOperands != 'o' && mNum != 0.0) {
            mNum = calculator(mPreOperands, mOperator, mNum);
        }
        Button btn = (Button) v;
        this.mOperator = btn.getText().charAt(0);
        mPreOperands = mNum;
        mNumberBufStr.delete(0, mNumberBufStr.length());
    }

    /**
     * Button OnClickListener do Operator button click logic
     */
    public void doEqualBtnClicked(View v) {
        mNum = calculator(mPreOperands, mOperator, mNum);
        String ans = String.valueOf(mNum).replace(".0", "");
        mOperator = 'o';
        mNumberBufStr.delete(0, mNumberBufStr.length());
        mShowResultEdt.setText("" + ans);
    }

    /**
     * Button OnClickListener do clear button click logic
     */
    public void doClearBtnClicked(View v) {
        doDataReset();
    }

    /**
     * Button OnClickListener do backspace button click logic
     */
    public void doBackspaceBtnClicked(View v) {
        if (mNumberBufStr.length() > 0) {
            mNumberBufStr.delete(mNumberBufStr.length() - 1, mNumberBufStr.length());
            mShowResultEdt.setText(mNumberBufStr);
        }
    }

    /**
     * Initial data
     */
    private void doDataReset() {
        mNumberBufStr.delete(0, mNumberBufStr.length());
        mOperator = 'o';
        mNum = 0.0;
        mPreOperands = 0.0;
        mShowResultEdt.setText("");
    }
}