package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class MainActivity extends Activity {

    private TextView mAnsView;
    private static final int DEF_DIV_SCALE = 10;
    private Toast mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAnsView = (TextView) findViewById(R.id.ansTextView);
        getActionBar().hide();
        mToast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
    }

    /**
     * show user a warning Toast message
     * 
     * @param str
     *            String
     */
    private void showToast(String str) {
        mToast.setText(str);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * Get Operator char index in mAnsView
     */
    private int getOperatorIndex() {
        String ansStr = mAnsView.getText().toString();
        int operatorIndex = -1;
        if (ansStr.length() < 2)
            return operatorIndex;
        for (char c : "+-*/".toCharArray()) {
            // avoid negative number and exponent number
            int indexE = ansStr.indexOf("E-");
            operatorIndex = (indexE == -1) ? ansStr.indexOf(c, 1) : ansStr.indexOf(c, indexE + 2);
            if (operatorIndex != -1) {
                break;
            }
        }
        return operatorIndex;
    }

    /**
     * Button OnClickListener do number button click logic
     */
    public void doNumberBtnClicked(View v) {
        String ansStr = mAnsView.getText().toString();
        int operatorIndex = getOperatorIndex();
        Button btn = (Button) v;
        char numberChar = btn.getText().charAt(0);
        String lastNumberStr = ansStr.substring((operatorIndex == -1) ? 0 : operatorIndex + 1, ansStr.length());
        if (numberChar == '.' && (lastNumberStr.length() == 0 || lastNumberStr.indexOf('.') != -1)) {
            return;
        }
        mAnsView.setText(ansStr + numberChar);
    }

    /**
     * calculator
     */
    private String calculator(String preOperand, char operator, String postOperand) {
        BigDecimal preOperandBD = new BigDecimal(preOperand);
        BigDecimal postOperandBD = new BigDecimal(postOperand);
        String ansStr = "";
        switch (operator) {
        case '+':
            ansStr = String.valueOf(preOperandBD.add(postOperandBD).doubleValue());
            break;
        case '-':
            ansStr = String.valueOf(preOperandBD.subtract(postOperandBD).doubleValue());
            break;
        case '*':
            ansStr = String.valueOf(preOperandBD.multiply(postOperandBD).doubleValue());
            break;
        case '/':
            ansStr = String
                    .valueOf(preOperandBD.divide(postOperandBD, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue());
            break;
        default:
            break;
        }
        return ansStr.endsWith(".0") ? ansStr.substring(0, ansStr.length() - 2) : ansStr;
    }

    /**
     * Button OnClickListener do Operator button click logic
     */
    public void doOperatorBtnClicked(View v) {
        String ansStr = mAnsView.getText().toString();
        int preOperatorIndex = getOperatorIndex();
        Button btn = (Button) v;
        char operator = btn.getText().charAt(0);
        if (ansStr.length() == 0) {
            return;
        }
        if (preOperatorIndex == ansStr.length() - 1) {
            ansStr = ansStr.substring(0, ansStr.length() - 1);
        } else if (preOperatorIndex != -1){
            String preOperand = ansStr.substring(0, preOperatorIndex);
            char preOperator = ansStr.charAt(preOperatorIndex);
            String postOperand = ansStr.substring(preOperatorIndex + 1, ansStr.length());
            if (preOperator == '/' && new BigDecimal(postOperand).compareTo(BigDecimal.ZERO) == 0) {
                showToast("Unable to calculate (division by zero)!");
                return;
            }
            ansStr = calculator(preOperand, preOperator, postOperand);
        }
        mAnsView.setText(ansStr + ((operator == '=') ? "" : operator));
    }

    /**
     * Button OnClickListener do clear button click logic
     */
    public void doClearBtnClicked(View v) {
        mAnsView.setText("");
    }

    /**
     * Button OnClickListener do backspace button click logic
     */
    public void doBackspaceBtnClicked(View v) {
        String ansStr = mAnsView.getText().toString();
        if (ansStr.length() == 0) {
            return;
        }
        ansStr = ansStr.substring(0, ansStr.length() - 1);
        mAnsView.setText(ansStr);
    }
}