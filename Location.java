public class Location {
    public String optional = null;
    public Double longitude;
    public Double latitude;
    public Location(Double lat,Double lon){
        this.longitude = lon;
        this.latitude =  lat;
        String latString = String.format("%.6f",lat);
        String lonString = String.format("%.6f",lon);
        System.out.println(latString + " " + lonString);
    }
    public Double returnLong(){
        return this.longitude;
    }
    public Double returnLat(){
        return this.latitude;
    }
    public String returnOptional(){
        return this.optional;
    }
    public void setOptional(String optional){
        this.optional = optional;
    }
}
