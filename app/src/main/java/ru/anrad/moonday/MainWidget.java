package ru.anrad.moonday;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.TextView;

import ru.anrad.moonday.dao.CurrentDayDataSource;
import ru.anrad.moonday.dao.HistoryDataSource;
import ru.anrad.moonday.dao.MoonDayStatistic;

/**
 * Implementation of App Widget functionality.
 */
public class MainWidget extends AppWidgetProvider {
    static String widgetText = "???";
    static int colorInt = Color.DKGRAY;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        calcWidgetParams(context);

        // Construct the RemoteViews object
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.main_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        widgetView.setTextViewText(R.id.appwidget_text, widgetText);
        //widgetView.setTextColor(R.id.appwidget_text, colorInt);
        widgetView.setInt(R.id.appwidget_text, "setBackgroundColor", colorInt);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        widgetView.setOnClickPendingIntent(R.id.appwidget_text, pIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widgetView);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void calcWidgetParams(Context context) {
        /*TODO
        Определить есть ли данные по текущему дню
         - если данных нет то серый квадрат с вопросом
         - если день активен, то красный квадрат с (getEndFosecastDaysLeft(current.getBegin)
         - если день закрыт, то зеленый квадрат с (getBeginFosecastDaysLeft(current.getEnd)
         -- если статистики нет, то только квадраты
        */
        CurrentDayDataSource currentDS = CurrentDayDataSource.getInstance(context);
        MoonDayStatistic stat = HistoryDataSource.getInstance(context).getStatistic();

        if (! currentDS.isEmpty()) {
            if (currentDS.isActive()) {
                if (stat != null) widgetText = String.valueOf(stat.getEndForecastLeftDays(currentDS.getBegin()));
                colorInt = context.getResources().getColor(R.color.colorDRRed);
            } else {
                if (stat != null) widgetText = String.valueOf(stat.getBeginForecastLeftDays(currentDS.getEnd()));
                colorInt = context.getResources().getColor(R.color.colorDRGreen);
            }
        } else {
            widgetText = "?";
            colorInt = Color.DKGRAY;
        }
    }
}

