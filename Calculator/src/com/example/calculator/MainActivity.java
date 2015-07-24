package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.calculator.R;

public class MainActivity extends Activity {

    private StringBuffer numberStr = null;
    private Character operator = null;
    private double num = 0.0;
    private double numtemp = 0.0;
    private EditText showResult=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showResult = (EditText) findViewById(R.id.result_id);
        
        //initial
        numberStr = new StringBuffer();
        reset();
    }
    
    public void btn0Clicked(View v) {
        insert('0');
    }
    
    public void btn1Clicked(View v) {
        insert('1');
    }

    public void btn2Clicked(View v) {
        insert('2');
    }

    public void btn3Clicked(View v) {
        insert('3');
    }

    public void btn4Clicked(View v) {
        insert('4');
    }

    public void btn5Clicked(View v) {
        insert('5');
    }

    public void btn6Clicked(View v) {
        insert('6');
    }

    public void btn7Clicked(View v) {
        insert('7');
    }

    public void btn8Clicked(View v) {
        insert('8');
    }

    public void btn9Clicked(View v) {
        insert('9');
    }
    
    public void btnfloatClicked(View v) {
        insert('.');
    }

    public void btnplusClicked(View v) {
        perform('+');
    }

    public void btnminusClicked(View v) {
        perform('-');
    }
    
    public void btnmultiClicked(View v) {
        perform('*');
    }

    public void btndivideClicked(View v) {
        perform('/');
    }


    public void btnequalClicked(View v) {
        calculate();
    }

    public void btnclearClicked(View v) {
        reset();
    }
    
    public void btnbkClicked(View v) {
        backspace();
    }

    private void reset() {
        numberStr.delete(0, numberStr.length());
        operator = 'o';
        num = 0.0;
        numtemp = 0.0;
        showResult.setText("");
    }
    
    private void backspace() {
        if(numberStr.length()>0){
//            numberStr = numberStr.substring(0, numberStr.length()-1);
            numberStr.delete(numberStr.length()-1, numberStr.length());
            showResult.setText(numberStr);
        }
    }

    private void insert(Character numberChar) {
//        numberStr = numberStr + numberChar;
        numberStr.append(numberChar);
        num = Double.valueOf(numberStr.toString()).doubleValue();
        showResult.setText(numberStr);
    }

    private void perform(Character operator) {
        numberStr.delete(0, numberStr.length());
        numtemp = num;
        this.operator=operator;
    }

    private void calculate() {
        if (operator == '+'){
            num = numtemp + num;
        }else if (operator == '-'){
            num = numtemp - num;
        }else if (operator == '*'){
            num = numtemp * num;
        }else if (operator == '/'){
            num = numtemp / num;
        }
        numberStr.delete(0, numberStr.length());
        showResult.setText("" + num);
    }

}
