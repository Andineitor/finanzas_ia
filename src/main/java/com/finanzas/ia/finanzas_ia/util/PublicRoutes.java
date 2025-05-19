package com.finanzas.ia.finanzas_ia.util;

import java.util.List;

public class PublicRoutes {
	 public static final List<String> WHITELIST = List.of(
		        "/api/auth/login",
		        "/api/auth/register",
		        "/login-user",
		        "/register-user",
		        "/",
		        "/css/", "/js/"
		    );

		    public static boolean isPublic(String path) {
		        return WHITELIST.stream().anyMatch(path::startsWith);
		    }

}
