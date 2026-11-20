import Vue from "vue";
import axios from "axios";
import router from "@/router";
import qs from "qs";
import merge from "lodash/merge";
import { clearLoginInfo } from "@/utils";
var types = ''
const http = axios.create({
	timeout: 1000 * 30,
	headers: {
		// "Content-Type": types,
		Accept: "*/*"
	}
});

/**
 * 请求拦截
 */
http.interceptors.request.use(
	config => {
		config.headers["token"] = Vue.cookie.get("token"); // 请求头带上token
		// console.log(config,'222222222')
		switch (config.method) {
			case 'post':
				if(types==='json'){ //json格式提交
					config.headers["Content-Type"] = 'application/json; charset=utf-8'
				}else{ //form表单
					config.headers["Content-Type"] = 'application/x-www-form-urlencoded; charset=utf-8'
				}
		}
		return config;
	},
	error => {
		return Promise.reject(error);
	}
);

/**
 * 响应拦截
 */
http.interceptors.response.use(
	response => {
		if (response.data && response.data.code === 401) {
			// 401, token失效
			sessionStorage.clear();
			clearLoginInfo();
			router.push({ name: "login" });
		}
		return response;
	},
	error => {
		return Promise.reject(error);
	}
);

/**
 * 请求地址处理
 * @param {*} actionName action方法名称
 */
http.adornUrl = (actionName) => {
  // 非生产环境 && 开启代理, 接口前缀统一使用[/proxyApi/]前缀做代理拦截!
  // return (process.env.NODE_ENV !== 'production' && process.env.OPEN_PROXY ? '/proxyApi/' : window.SITE_CONFIG.baseUrl) + actionName
	// return 'http://192.168.0.126:7777/sqx_fast/' + actionName
	return 'https://mpfxadmin.xianmaxiong.com/sqx_fast/' + actionName
	 // return '/sqx_fast/' + actionName
}
http.adornUrlUp = (actionName) => {
  // 非生产环境 && 开启代理, 接口前缀统一使用[/proxyApi/]前缀做代理拦截!
  // return (process.env.NODE_ENV !== 'production' && process.env.OPEN_PROXY ? '/proxyApi/' : window.SITE_CONFIG.baseUrl) + actionName
	// return 'http://192.168.0.120:8082/sqx_fast/' + actionName
	return 'https://mpfxadmin.xianmaxiong.com/sqx_fast/' + actionName
}

http.adornWss = (actionName) => {
  // 非生产环境 && 开启代理, 接口前缀统一使用[/proxyApi/]前缀做代理拦截!
  // return (process.env.NODE_ENV !== 'production' && process.env.OPEN_PROXY ? '/proxyApi/' : window.SITE_CONFIG.baseUrl) + actionName
	return 'wss://mpfxadmin.xianmaxiong.com/wss/' + actionName
	// return 'http://192.168.0.113:8082/sqx_fast/' + actionName
}

/**
 * get请求参数处理
 * @param {*} params 参数对象
 * @param {*} openDefultParams 是否开启默认参数?
 */
http.adornParams = (params = {}, openDefultParams = false) => {
	var defaults = {
		t: new Date().getTime()
	};
	return openDefultParams ? merge(defaults, params) : params;
};

/**
 * post请求数据处理
 * @param {*} data 数据对象
 * @param {*} openDefultdata 是否开启默认数据?
 * @param {*} contentType 数据格式
 *  json: 'application/json; charset=utf-8'
 *  form: 'application/x-www-form-urlencoded; charset=utf-8'
 */
http.adornData = (data = {}, openDefultdata = false, contentType = "json") => {
	var defaults = {
		t: new Date().getTime()
	};
	data = openDefultdata ? merge(defaults, data) : data;
	types = contentType
	return contentType === 'json' ? JSON.stringify(data) : qs.stringify(data)
};
export default http;
