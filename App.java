import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class App {
    public static ArrayList<Location> locationList = new ArrayList<Location>();
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<ArrayList<String>> stringList = new ArrayList<ArrayList<String>>();
        
        while (sc.hasNextLine()){
            strings.add(sc.nextLine());
        }
        for (String s : strings){
            stringList.add(new ArrayList<String>(Arrays.asList(s.split("\\s+"))));
        }
        
        for (ArrayList<String> s : stringList){
            if (s.size() == 2){
                twosizeMethod(s);
            }
            else if (s.size() == 3){
                System.out.println("Unable to process: " +  toString(s));
            }
            else if (s.size() == 4){
                ArrayList<String> stv = new ArrayList<String>();
                stv.addAll(s);
                String nullCheck = standardMethod(stv);
                if(nullCheck == null){
                    System.out.println("Unable to process: " +  toString(s));
                }
            }
            else if (s.size() >= 5){
                ArrayList<String> stv = new ArrayList<String>();
                stv.addAll(s);
                if (stv.get(1).matches("(.*),(.*)")){
                    stv.add(1, stv.get(1).replace(",",""));
                    stv.remove(2);
                }
                if (stv.get(3).matches("(.*),(.*)")){
                    stv.add(3, stv.get(3).replace(",",""));
                    stv.remove(4);
                }
                if (isNumeric(stv.get(0)) && isNumeric(stv.get(2)) && stv.get(1).matches("[NWSE]") && stv.get(3).matches("[NWSE]")){
                    String tempstring = "";
                    for (int i = 4; i < stv.size();i++){
                        tempstring += stv.get(i) + " ";
                    }
                    while (stv.size() != 4){
                        stv.remove(4);
                    }
                    stv.add(tempstring); //longer than 4 and meets requirements, gather all remaining as optional.
                    String nullCheck = standardMethod(s);
                    if(nullCheck == null){
                        System.out.println("Unable to process: " +  toString(s));
                    } else {
                        (locationList.get(locationList.size()-1)).setOptional(stv.get(4));
                    }
                }
                else if (stv.size() > 7){
                    if (stv.get(6).matches("(.*),(.*)")){
                        stv.add(6, stv.get(6).replace(",",""));
                        stv.remove(7);
                    }
                    if (stv.get(3).endsWith("N") || stv.get(3).endsWith("S") || stv.get(3).endsWith("W") || stv.get(3).endsWith("E") || stv.get(7).endsWith("N") || stv.get(7).endsWith("S") || stv.get(7).endsWith("N") || stv.get(7).endsWith("N")){
                        if (stv.get(0).contains("°")){
                            stv.add(0, stv.get(0).replace("°",""));
                            stv.remove(1);
                        }
                        if (stv.get(0).contains("Â")){
                            stv.add(0, stv.get(0).replace("Â",""));
                            stv.remove(1);
                        }
                        if (stv.get(1).contains("'")){
                            stv.add(1, stv.get(1).replace("'",""));
                            stv.remove(2);
                        }
                        if (stv.get(2).contains(""+'"')){
                            stv.add(2, stv.get(2).replace(""+'"',""));
                            stv.remove(3);
                        }
                        if (stv.get(4).contains("°")){
                            stv.add(4, stv.get(4).replace("°",""));
                            stv.remove(5);
                        }
                        if (stv.get(4).contains("Â")){
                            stv.add(4, stv.get(4).replace("Â",""));
                            stv.remove(5);
                        }
                        if (stv.get(5).contains("'")){
                            stv.add(5, stv.get(5).replace("'",""));
                            stv.remove(6);
                        }
                        if (stv.get(6).contains(""+'"')){
                            stv.add(6, stv.get(6).replace(""+'"',""));
                            stv.remove(7);
                        }
                        String nullCheck = DDMtoLL(stv);
                        if (nullCheck == null){
                            System.out.println("Unable to process: " +  toString(s));
                        }
                    }
                    else if(stv.size() > 13){
                        if (stv.get(6).endsWith("N") || stv.get(6).endsWith("S") || stv.get(6).endsWith("W") || stv.get(6).endsWith("E") || stv.get(13).endsWith("N") || stv.get(13).endsWith("S") || stv.get(13).endsWith("N") || stv.get(13).endsWith("N")){
                            ArrayList<String> dms = new ArrayList<String>();
                            boolean flag = true;
                            for (String sValues : stv){
                                if(!sValues.matches("[dms0-9NSWE,]+")){
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag){
                                dms.add(stv.get(0));
                                dms.add(stv.get(2));
                                dms.add(stv.get(4));
                                dms.add(stv.get(6));
                                dms.add(stv.get(7));
                                dms.add(stv.get(9));
                                dms.add(stv.get(11));
                                dms.add(stv.get(13));
                                String nullCheck = DDMtoLL(dms);
                                if (nullCheck == null){
                                    System.out.println("Unable to process: " + toString(s));
                                }
                            } else System.out.println("Unable to process: " +  toString(s));
                        }
                    } else System.out.println("Unable to process: " +  toString(s));
                } else System.out.println("Unable to process: " +  toString(s));
            } else System.out.println("Unable to process: " +  toString(s));
        }
        createJSON(locationList);
    }
    
    public static boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(string);
        } catch (NumberFormatException error) {
            return false;
        }
        return true;
    }

    public static void createJSON(ArrayList<Location> locationList){
        String jsonString ="{\n    \"type\": \"FeatureCollection\",\n    \"features\": [";
        int count = 1;
        for (Location L : locationList){
            jsonString = jsonString + "\n       {\n            \"type\": \"Feature\",\n            \"geometry\": {\n                \"type\": \"Point\",";
            jsonString = jsonString + "\n                \"coordinates\": [\n                    " + L.returnLong() + ",\n                    " + L.returnLat();
            jsonString = jsonString + "\n                ]\n            }";
            if (L.returnOptional() != null){
                jsonString = jsonString + ",\n            \"properties\": {\n                \"name\": " + '"'+ L.returnOptional() +'"'+ "\n            }\n        }";
            } else {
                jsonString = jsonString + ",\n            \"properties\": {\n                \"name\": " + '"' + '"'+ "\n            }\n        ";
            }
            if (count < locationList.size()){
                jsonString = jsonString + "\n    }";
                jsonString = jsonString + ",";
            }
            count++;
        }
        jsonString = jsonString + "\n     ]\n}";  
        try (FileWriter file = new FileWriter("mapdata.json")) {
            file.write(jsonString); 
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String toString(ArrayList<String> strings){
        String string = "";
        for (String s : strings){
            string = string + s +  " ";
        }
        return string;
    }

    public static void twosizeMethod(ArrayList<String> stv){
        ArrayList<String> s = new ArrayList<String>();
        s.addAll(stv);
        if (s.get(0).endsWith(",")){
            s.add(0, s.get(0).replace(",",""));
            s.remove(1);
        }
        if (s.get(0).endsWith("N") || s.get(0).endsWith("S") || s.get(0).endsWith("W") || s.get(0).endsWith("E") || s.get(1).endsWith("N") || s.get(1).endsWith("S") || s.get(1).endsWith("E") || s.get(1).endsWith("W")){
            if (s.get(0).contains("°")){
                s.add(0, s.get(0).replace("°"," "));
                s.remove(1);
            }
            if (s.get(0).contains("Â")){
                s.add(0, s.get(0).replace("Â"," "));
                s.remove(1);
            }
            if (s.get(0).contains("'")){
                s.add(0, s.get(0).replace("'"," "));
                s.remove(1);
            }
            if (s.get(0).contains(""+'"')){
                s.add(0, s.get(0).replace(""+'"'," "));
                s.remove(1);
            }
            if (s.get(1).contains("°")){
                s.add(1, s.get(1).replace("°"," "));
                s.remove(2);
            }
            if (s.get(1).contains("Â")){
                s.add(1, s.get(1).replace("Â"," "));
                s.remove(2);
            }
            if (s.get(1).contains("'")){
                s.add(1, s.get(1).replace("'"," "));
                s.remove(2);
            }
            if (s.get(1).contains(""+'"')){
                s.add(1, s.get(1).replace(""+'"'," "));
                s.remove(2);
            }
            String[] s1 = s.get(0).split("\\s+");
            String[] s2 = s.get(1).split("\\s+");
            ArrayList<String> strings = new ArrayList<String>();
            for (String st : s1){
                strings.add(st);
            }
            for (String st : s2){
                strings.add(st);
            }
            String nullCheck = DDMtoLL(strings);
            if (nullCheck == null){
                System.out.println("Unable to process: " +  toString(stv));
            }
        }
        else {
            try{
                if (Double.parseDouble(s.get(0))<= -90.000001 || Double.parseDouble(s.get(0))>= 90.000001) System.out.println("Unable to process: " +  toString(stv));
                else if (Double.parseDouble(s.get(1))<= -180.000001 || Double.parseDouble(s.get(1))>= 180.000001) System.out.println("Unable to process: " +  toString(stv));
                else {
                    Location location = new Location(Double.parseDouble(s.get(0)),Double.parseDouble(s.get(1)));
                    locationList.add(location);
                }

            } catch (NumberFormatException e){
                System.out.println("Unable to process: " +  toString(stv));
            }
        }
    }

    public static String DDMtoLL(ArrayList<String> strings){
        if (isNumeric(strings.get(0)) && isNumeric(strings.get(1)) && isNumeric(strings.get(2)) && isNumeric(strings.get(4)) && isNumeric(strings.get(5)) && isNumeric(strings.get(6))){
            Double degree1 = Double.parseDouble(strings.get(0));
            Double minute1 = Double.parseDouble(strings.get(1));
            Double second1 = Double.parseDouble(strings.get(2));
            Double degree2 = Double.parseDouble(strings.get(4));
            Double minute2 = Double.parseDouble(strings.get(5));
            Double second2 = Double.parseDouble(strings.get(6));
            if (second1 <= -0.000001 || second1 >= 60) return null;
            if (second2 <= -0.000001 || second2 >= 60) return null;
            if (minute1 <= -0.000001 || minute1 >= 60) return null;
            if (minute2 <= -0.000001 || minute2 >= 60) return null;
            Double decimalDegree1 = degree1 + minute1/60 + second1/3600;
            Double decimalDegree2 = degree2 + minute2/60 + second2/3600;
            if (strings.get(3).endsWith("N") && strings.get(7).endsWith("E")){
                Location L = new Location(decimalDegree1, decimalDegree2);
                locationList.add(L);
            }
            else if (strings.get(3).endsWith("N") && strings.get(7).endsWith("W")){
                Location L = new Location(decimalDegree1,-1.0 * decimalDegree2);
                locationList.add(L);
            }
            else if (strings.get(3).endsWith("S") && strings.get(7).endsWith("E")){
                Location L = new Location(-1.0 * decimalDegree1, decimalDegree2);
                locationList.add(L);
            }
            else if (strings.get(3).endsWith("S") && strings.get(7).endsWith("W")){
                Location L = new Location(-1.0 * decimalDegree1,-1.0 * decimalDegree2);
                locationList.add(L);
            }
            else if (strings.get(3).endsWith("E") && strings.get(7).endsWith("N")){
                Location L = new Location(decimalDegree2, decimalDegree1);
                locationList.add(L);
            }
            else if (strings.get(3).endsWith("E") && strings.get(7).endsWith("S")){
                Location L = new Location(-1.0 * decimalDegree2, decimalDegree1);
                locationList.add(L);
            }
            else if (strings.get(3).endsWith("W") && strings.get(7).endsWith("N")){
                Location L = new Location(decimalDegree2,-1.0 * decimalDegree1);
                locationList.add(L);
            }
            else if (strings.get(3).endsWith("W") && strings.get(7).endsWith("S")){
                Location L = new Location(-1.0 * decimalDegree2,-1.0 * decimalDegree1);
                locationList.add(L);
            }
        } else return null;
        return "T";
    }

    public static String standardMethod(ArrayList<String> s){
        if (s.get(1).matches("(.*),(.*)")){
            s.add(1, s.get(1).replace(",",""));
            s.remove(2);
        }
        if (s.get(3).matches("(.*),(.*)")){
            s.add(3 ,s.get(3).replace(",",""));
            s.remove(4);
        }
        if (isNumeric(s.get(0)) && isNumeric(s.get(2)) && s.get(1).matches("[NWSEnswe]") && s.get(3).matches("[NWSEnswe]")){
            if (s.get(0).contains("+") || s.get(2).contains("+") || s.get(0).contains("-") || s.get(2).contains("-")) return null;
            else if (s.get(1).matches("[Nn]")){
                if (Double.parseDouble(s.get(0))<= -90.000001 || Double.parseDouble(s.get(0))>= 90.000001) return null;
                else if (Double.parseDouble(s.get(2))<= -180.000001 || Double.parseDouble(s.get(2))>= 180.000001) return null;
                else if (s.get(3).matches("[Ee]")){
                    Location location = new Location(Double.parseDouble(s.get(0)),Double.parseDouble(s.get(2)));
                    locationList.add(location);
                }
                else if (s.get(3).matches("[Ww]")){
                    Location location = new Location(Double.parseDouble(s.get(0)),-1.0 *Double.parseDouble(s.get(2)));
                    locationList.add(location);
                }
                else return null;
            }
            else if (s.get(1).matches("[Ss]")){
                if (Double.parseDouble(s.get(0))<= -90.000001 || Double.parseDouble(s.get(0))>=90.000001) return null;
                else if (Double.parseDouble(s.get(2))<= -180.000001 || Double.parseDouble(s.get(2))>=180.000001) return null;
                else if (s.get(3).matches("[Ee]")){
                    Location location = new Location(-1.0 * Double.parseDouble(s.get(0)),Double.parseDouble(s.get(2)));
                    locationList.add(location);
                }
                else if (s.get(3).matches("[Ww]")){
                    Location location = new Location(-1.0 * Double.parseDouble(s.get(0)),-1.0 * Double.parseDouble(s.get(2)));
                    locationList.add(location);
                }
                else return null;
            }
            else if (s.get(1).matches("[Ww]")){
                if (Double.parseDouble(s.get(0))<= -180.000001 || Double.parseDouble(s.get(0))>=180.000001) return null;
                else if (Double.parseDouble(s.get(2))<= -90.000001 || Double.parseDouble(s.get(2))>=90.000001) return null;
                else if (s.get(3).matches("[Nn]")){
                    Location location = new Location(Double.parseDouble(s.get(2)),-1.0 * Double.parseDouble(s.get(0)));
                    locationList.add(location);
                }
                else if (s.get(3).matches("[Ss]")){
                    Location location = new Location(-1.0 * Double.parseDouble(s.get(2)),-1.0 * Double.parseDouble(s.get(0)));
                    locationList.add(location);
                }
                else return null;
            }
            else if (s.get(1).matches("[Ee]")){
                if (Double.parseDouble(s.get(0))<= -180.000001 || Double.parseDouble(s.get(0))>=180.000001) return null;
                else if (Double.parseDouble(s.get(2))<= -90.000001 || Double.parseDouble(s.get(2))>=90.000001) return null;
                else if (s.get(3).matches("[Nn]")){
                    Location location = new Location(Double.parseDouble(s.get(2)),Double.parseDouble(s.get(0)));
                    locationList.add(location);
                }
                else if (s.get(3).matches("[Ss]")){
                    Location location = new Location(-1.0 * Double.parseDouble(s.get(2)),Double.parseDouble(s.get(0)));
                    locationList.add(location);
                }
                else return null;
            }
        } else {
            return null;
        }
    return "T";
    }
}
