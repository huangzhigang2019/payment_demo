/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology (Shenzhen) Co., Ltd. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology (Shenzhen) Co., Ltd. and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2017-2023 PAX Computer Technology (Shenzhen) Co., Ltd. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date                         Author                        Action
 * 2017/12/20                   PAX                     Create/Add/Modify/Delete
 * ===========================================================================================
 */

package com.pax.demo.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pax.demo.R;

import java.util.List;

public class BackListAdapter extends BaseAdapter {

    private List<String> list = null;
    private Context context;
    private int pos = -1;

    public BackListAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_text, parent, false);
            holder = new Holder();
            holder.textView = (TextView) convertView.findViewById(R.id.fragment_textview);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (pos == position) {
            holder.textView.setTextColor(Color.BLUE);
        } else {
            holder.textView.setTextColor(Color.BLACK);
        }
        holder.textView.setTextSize(18);
        holder.textView.setPadding(0, 15, 0, 15);
        holder.textView.setText(list.get(position));
        return convertView;
    }

    class Holder {
        TextView textView;
    }

}
