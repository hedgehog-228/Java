
package com.mycompany.kitchenhelper;

import java.io.IOException;

public interface FileParser {
    Recipe parse(String source) throws IOException; 
    
}
