package com.test.project.util;

import java.io.File;
import java.io.FilenameFilter;

public class ExtensionFilter implements FilenameFilter{

private String extend;

public ExtensionFilter(String extend){
this.extend = extend;
}

@Override
public boolean accept(File dir, String name) { 
return name.endsWith(extend);
}

}
