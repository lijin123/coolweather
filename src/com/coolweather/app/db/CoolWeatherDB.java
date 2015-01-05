package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 单例模式=》构造方法私有化=》提供getInstance方法获取该CoolWeatherDB实例，保证了全局范围内只会有一个CoolWeatherDB实例
 * @author lijin
 *
 */
public class CoolWeatherDB
{
	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "cool_weather";

	/**
	 * 数据库版本号
	 */
	public static final int VERSION = 1;

	private SQLiteDatabase db;
	private static CoolWeatherDB coolWeatherDB;

	/**
	 * 将构造方法私有化
	 */
	private CoolWeatherDB(Context context)
	{
		CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(
				context, DB_NAME, null, VERSION);
		db = coolWeatherOpenHelper.getWritableDatabase();
	}

	/**
	 * 获取CoolWeatherDB的实例
	 */
	public synchronized static CoolWeatherDB getInstance(Context context)
	{
		if (coolWeatherDB == null)
		{
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/**
	 * 将Province实例存储到数据库
	 */
	public void saveProvince(Province province)
	{
		if (province != null)
		{
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProviceName());
			values.put("province_code", province.getProviceCode());
			db.insert("Province", null, values);
		}
	}

	/**
	 * 从数据库中读取所有的省份信息,cursor起初在查询出的第一条结果的上方
	 */
	public List<Province> loadProvinces()
	{
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst())
		{
			do
			{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProviceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				province.setProviceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 将City实例存储到数据库
	 * 
	 * @param city
	 */
	public void saveCity(City city)
	{
		if (city != null)
		{
			ContentValues contentValues = new ContentValues();
			contentValues.put("city_name", city.getCityName());
			contentValues.put("city_code", city.getCityCode());
			contentValues.put("province_id", city.getProvinceId());
			db.insert("City", null, contentValues);
		}
	}

	/**
	 * 从数据库中取下某省所有的城市信息
	 */
	public List<City> loadCities(int provinceId)
	{
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?", new String[]
		{ String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst())
		{
			do
			{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 将county实例存储到数据库
	 * 
	 * @param county
	 */
	public void saveCounty(County county)
	{
		if (county != null)
		{
			ContentValues contentValues = new ContentValues();
			contentValues.put("county_name", county.getCountyName());
			contentValues.put("county_code", county.getCountyCode());
			contentValues.put("city_id", county.getCityId());
		}
	}

	/**
	 * 根据城市id将所有县遍历出来
	 * 
	 * @param cityId
	 * @return
	 */
	public List<County> loadCounties(int cityId)
	{
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id=?", new String[]
		{ String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst())
		{
			do
			{
				County county = new County();
				county.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				county.setCityId(cityId);
			} while (cursor.moveToNext());
		}
		return list;
	}
}
