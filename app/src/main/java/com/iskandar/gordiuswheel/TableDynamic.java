package com.iskandar.gordiuswheel;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TableDynamic {

    private TableLayout tableLayout;
    private Context context;

    private String[]header;
    private ArrayList<String[]>data=new ArrayList<>();

    private TableRow tableRow;
    private TextView txtCell;

    private int indexC;
    private int indexR;

    private boolean mutiColor=false;
    int firstcolor= Color.rgb(19,0,255);
    int secondColor= Color.rgb(131,121,255);

    public TableDynamic(TableLayout tableLayout, Context context) {
        this.tableLayout=tableLayout;
        this.context=context;
    }


    public void addHeader(String[]header){
        this.header=header;
        crateHeader();
    }
    public void addData(ArrayList<String[]>data, int i){
        this.data=data;
        crateDataTable(i);
    }
    private void newRow(){
        tableRow= new TableRow(context);
    }
    private void newCell(){
        txtCell= new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(25);
    }
    private void crateHeader(){
        indexC=0;
        newRow();
        while (indexC<header.length){
            newCell();
            txtCell.setText(header[indexC++]);
            txtCell.setTextColor(Color.WHITE);
            tableRow.addView(txtCell, newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }

    private void crateDataTable(int x){
        String info;
        for (indexR=1;indexR<=x;indexR++) {
            newRow();
            for (indexC=0;indexC<header.length;indexC++) {
                newCell();
                String[] row = data.get(indexR-1);
                info=(indexC<row.length)?row[indexC]:"";
                txtCell.setText(info);
                tableRow.addView(txtCell,newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

    public void addItems(String[]item){
        String info;
        data.add(item);
        indexC=0;
        newRow();
        while (indexC<header.length){
            newCell();
            info=(indexC<header.length)?item[indexC++]:"";
            txtCell.setText(info);
            txtCell.setTextColor(Color.WHITE);
            tableRow.addView(txtCell,newTableRowParams());
        }
        tableLayout.addView(tableRow, data.size());
        recoloring();
    }

    public void backgroundHeader(int color){
        indexC=0;
        while (indexC<header.length){
            txtCell=getCell(0,indexC++);
            txtCell.setBackgroundColor(color);
        }
    }

    public void backgroudData(int firstcolor, int secondColor){
        for (indexR=1;indexR<=header.length;indexR++) {
            mutiColor=!mutiColor;
            for (indexC=0;indexC<header.length;indexC++) {
                txtCell = getCell(indexR, indexC);
                txtCell.setBackgroundColor((mutiColor) ? firstcolor : secondColor);
            }
        }
        this.firstcolor=firstcolor;
        this.secondColor=secondColor;
    }

    public void recoloring(){
        indexC=0;
        mutiColor=!mutiColor;
        while (indexC<header.length){
            txtCell=getCell(data.size(),indexC++);
            txtCell.setBackgroundColor((mutiColor)?firstcolor:secondColor);
        }
    }

    private TableRow getRow(int index){
        return (TableRow)tableLayout.getChildAt(index);
    }

    private TextView getCell(int rowIndex, int columIndex){
        tableRow=getRow(rowIndex);
        return (TextView)tableRow.getChildAt(columIndex);
    }


    private TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params=new TableRow.LayoutParams();
        params.setMargins(1,1,1,1);
        params.weight=1;
        return params;
    }
}
