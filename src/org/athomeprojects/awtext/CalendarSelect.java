//// // // // // Moira - A Chinese Astrology Charting Program// Copyright (C) 2004-2015 At Home Projects//// This program is free software; you can redistribute it and/or modify// it under the terms of the GNU General Public License as published by// the Free Software Foundation; either version 2 of the License, or// (at your option) any later version.//// This program is distributed in the hope that it will be useful,// but WITHOUT ANY WARRANTY; without even the implied warranty of// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the// GNU General Public License for more details.//// You should have received a copy of the GNU General Public License// along with this program; if not, write to the Free Software// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA//package org.athomeprojects.awtext;import java.awt.Color;import java.awt.FlowLayout;import java.awt.Font;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.text.DateFormatSymbols;import java.text.DecimalFormat;import java.util.Calendar;import javax.swing.JCheckBox;import javax.swing.JComboBox;import javax.swing.JLabel;import javax.swing.JPanel;import org.athomeprojects.base.BaseCalendar;import org.athomeprojects.base.Resource;public class CalendarSelect extends JPanel implements ActionListener {	private static final long serialVersionUID = 7664935318972208297L;	private final int YEAR_START = 1900;	private final int YEAR_END = 2100;	private final int YEAR_WIDTH = 60;	private Calendar calendar = Calendar.getInstance();	private JCheckBox check_box;	private ActionListener listener;	private JComboBox month, day, hour, minute, am_pm;	private EditCombo year;	private DecimalFormat fill_format = new DecimalFormat("00");	public void init(String name, boolean check) {		String font_name = Resource.getFontName();		setBackground(Color.white);		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));		listener = null;		if (name != null) {			if (check) {				check_box = new JCheckBox(name);				check_box.setBackground(Color.white);				check_box.setFont(new Font(font_name, Font.BOLD, 14));				add(check_box);				check_box.addActionListener(this);			} else {				JLabel label = new JLabel(name);				label.setFont(new Font(font_name, Font.BOLD, 14));				add(label);			}		}		Font font = new Font(font_name, Font.PLAIN, 12);		{			DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();			String[] names = dateFormatSymbols.getShortMonths();			month = new JComboBox(names);			addCombo(month, font, Resource.getString("tip_select_month"));		}		{			String[] days = new String[31];			for (int i = 1; i <= 31; i++) {				days[i - 1] = Integer.toString(i);			}			day = new JComboBox(days);			addCombo(day, font, Resource.getString("tip_select_day"));		}		{			String[] years = new String[YEAR_END - YEAR_START + 1];			for (int i = YEAR_START; i <= YEAR_END; i++) {				years[i - YEAR_START] = Integer.toString(i);			}			year = new EditCombo(years);			year.setPreferredWidth(YEAR_WIDTH);			addCombo(year, font, Resource.getString("tip_select_year"));		}		{			String[] hours = new String[12];			for (int i = 0; i < 12; i++) {				hours[i] = (i == 0) ? "12" : fill_format.format(i);			}			hour = new JComboBox(hours);			addCombo(hour, font, Resource.getString("tip_select_hour"));		}		{			JLabel colon = new JLabel(":");			colon.setFont(font);			add(colon);		}		{			String[] minutes = new String[60];			for (int i = 0; i < 60; i++) {				minutes[i] = fill_format.format(i);			}			minute = new JComboBox(minutes);			addCombo(minute, font, Resource.getString("tip_select_minute"));		}		{			am_pm = new JComboBox();			DateFormatSymbols symbol = new DateFormatSymbols();			String[] am_pm_name = symbol.getAmPmStrings();			am_pm.addItem(am_pm_name[0]);			am_pm.addItem(am_pm_name[1]);			addCombo(am_pm, font, Resource.getString("tip_select_ampm"));		}		setDate();	}	private void addCombo(JComboBox combo, Font font, String tool_tip) {		combo.setFont(font);		combo.setBackground(Color.white);		combo.setToolTipText(tool_tip);		add(combo);	}	public void getCalendar(int[] date) {		getDate();		BaseCalendar.getCalendar(calendar, date);	}	public void setCalendar(int[] date) {		if (date == null) {			calendar.setTime(Calendar.getInstance().getTime());		} else {			calendar.set(Calendar.YEAR, date[0]);			calendar.set(Calendar.MONTH, date[1] - 1);			calendar.set(Calendar.DAY_OF_MONTH, date[2]);			calendar.set(Calendar.HOUR_OF_DAY, date[3]);			calendar.set(Calendar.MINUTE, date[4]);		}		setDate();	}	public void reset() {		setCalendar(null);	}	protected void getDate() {		int y = year.getSelectedIndex();		int m = month.getSelectedIndex();		int d = day.getSelectedIndex();		int h = hour.getSelectedIndex();		int u = minute.getSelectedIndex();		int s = am_pm.getSelectedIndex();		if (y < 0) {			String str = (String) year.getSelectedItem();			try {				y = Integer.parseInt(str);			} catch (NumberFormatException e) {				y = 0;			}			if (y <= 0) {				y = 1;				year.setSelectedItem(Integer.toString(y));			}		} else {			y += YEAR_START;		}		if (m < 0)			m = matchSelection(month);		if (d < 0)			d = matchSelection(day);		if (h < 0)			h = matchSelection(hour);		if (u < 0)			u = matchSelection(minute);		if (s < 0)			s = matchSelection(am_pm);		if (s == 1) {			h += 12;		}		calendar.set(y, m, d + 1, h, u);	}	protected int matchSelection(JComboBox combo) {		String key = ((String) combo.getSelectedItem());		int n = combo.getItemCount();		for (int i = 0; i < n; i++) {			String str = (String) combo.getItemAt(i);			if (str.equalsIgnoreCase(key))				return i;		}		return 0;	}	protected void setDate() {		int y = calendar.get(Calendar.YEAR);		int m = calendar.get(Calendar.MONTH);		int d = calendar.get(Calendar.DAY_OF_MONTH);		int h = calendar.get(Calendar.HOUR_OF_DAY);		int u = calendar.get(Calendar.MINUTE);		if (y >= YEAR_START && y <= YEAR_END) {			year.setSelectedIndex(y - YEAR_START);		} else {			year.setSelectedItem(Integer.toString(y));		}		month.setSelectedIndex(m);		day.setSelectedIndex(d - 1);		minute.setSelectedIndex(u);		if (h >= 12) {			h -= 12;			am_pm.setSelectedIndex(1);		} else {			am_pm.setSelectedIndex(0);		}		hour.setSelectedIndex(h);	}	public void actionPerformed(ActionEvent event) {		boolean state = check_box.isSelected();		month.setEnabled(state);		day.setEnabled(state);		year.setEnabled(state);		hour.setEnabled(state);		minute.setEnabled(state);		am_pm.setEnabled(state);		if (listener != null) {			listener.actionPerformed(new ActionEvent(this, event.getID(), event					.getActionCommand()));		}	}	public boolean isSelected() {		return check_box.isSelected();	}	public void setSelected(boolean check) {		check_box.setSelected(check);	}	public void addActionListener(ActionListener l) {		listener = l;	}}