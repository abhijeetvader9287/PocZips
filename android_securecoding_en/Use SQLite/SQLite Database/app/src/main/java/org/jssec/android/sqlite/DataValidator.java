/*
 * Copyright (C) 2012-2017 Japan Smartphone Security Association
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jssec.android.sqlite;

public class DataValidator {
    //Validate the Input value
    //validate numeric characters
    public static boolean validateNo(String idno) {
        //null and blank are OK
        if (idno == null || idno.length() == 0) {
           return true;
        }

        //Validate that it's numeric character.
        try {
            if (!idno.matches("[1-9][0-9]*")) {
                //Error if it's not numeric value 
                return false;
            }
        } catch (NullPointerException e) {
            //Detected an error
            return false;
        }

        return true;
    }

    // Validate the length of a character string
    public static boolean validateLength(String str, int max_length) {
       //null and blank are OK
       if (str == null || str.length() == 0) {
               return true;
       }

       //Validate the length of a character string is less than MAX
       try {
           if (str.length() > max_length) {
               //When it's longer than MAX, error
               return false;
           }
       } catch (NullPointerException e) {
           //Bug
           return false;
       }

       return true;
    }

   // Validate the Input value
    public static boolean validateData(String idno, String name, String info) {
       if (!validateNo(idno)) {
           return false;
        }
       if (!validateLength(name, CommonData.TEXT_DATA_LENGTH_MAX)) {
           return false;
        }else if(!validateLength(info, CommonData.TEXT_DATA_LENGTH_MAX)) {
           return false;
        }    
       return true;
    }
}
