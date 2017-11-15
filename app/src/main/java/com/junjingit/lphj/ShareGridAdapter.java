package com.junjingit.lphj;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by niufan on 17/8/25.
 */

public class ShareGridAdapter extends BaseAdapter
{
    private Context context;
    
    private List<ShareModel> list;
    
    public ShareGridAdapter(Context context)
    {
        this.context = context;
    }
    
    public void setList(List<ShareModel> list)
    {
        this.list = list;
    }
    
    @Override
    public int getCount()
    {
        return list.size();
    }
    
    @Override
    public Object getItem(int i)
    {
        return null;
    }
    
    @Override
    public long getItemId(int i)
    {
        return 0;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        ShareHolder holder = null;
        if (null == convertView)
        {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.popup_content_layout, null);
            
            holder = new ShareHolder();
            
            holder.icon = convertView.findViewById(R.id.share_icon);
            holder.name = convertView.findViewById(R.id.share_name);
            
            convertView.setTag(holder);
        }
        else
        {
            holder = (ShareHolder) convertView.getTag();
        }
        
        holder.icon.setImageResource(list.get(position).shareIconId);
        holder.name.setText(list.get(position).shareName);
        
        return convertView;
    }
    
    public class ShareHolder
    {
        ImageView icon;
        
        TextView name;
        
    }
    
}
