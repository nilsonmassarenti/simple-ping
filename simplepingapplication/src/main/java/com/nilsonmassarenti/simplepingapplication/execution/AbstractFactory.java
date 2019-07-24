package com.nilsonmassarenti.simplepingapplication.execution;

import java.util.Properties;

public interface AbstractFactory<T> {
	T create(String os, Properties prop, String host);
}
