package com.project.thevoice;

public class DomainCheck
{
    public boolean domainCheck(String university, String emailid)
    {
        if (university.contains("Algoma University") && emailid.contains("algomau.ca")) {
            return true;
        } else if (university.contains("Brock University") && emailid.contains("brocku.ca")) {
            return true;
        } else if (university.contains("Carleton University") && emailid.contains("carleton.ca")) {
            return true;
        } else if (university.contains("Lakehead University") && emailid.contains("lakeheadu.ca")) {
            return true;
        } else if (university.contains("Laurentian University") && emailid.contains("laurentian.ca")) {
            return true;
        } else if (university.contains("McMaster University") && emailid.contains("mcmaster.ca")) {
            return true;
        } else if (university.contains("Nipissing University") && emailid.contains("nipissingu.ca")) {
            return true;
        } else if (university.contains("OCAD University") && emailid.contains("ocadu.ca")) {
            return true;
        } else if (university.contains("Ontario Tech University") && emailid.contains("ontariotechu.ca")) {
            return true;
        } else if (university.contains("Queen's University") && emailid.contains("queensu.ca")) {
            return true;
        } else if (university.contains("Royal Military College of Canada") && emailid.contains("rmc.ca")) {
            return true;
        } else if (university.contains("Ryerson University") && emailid.contains("ryerson.ca")) {
            return true;
        } else if (university.contains("Trent University") && emailid.contains("trentu.ca")) {
            return true;
        } else if (university.contains("University of Guelph") && emailid.contains("uoguelph.ca")) {
            return true;
        } else if (university.contains("University of Ottawa") && emailid.contains("uottawa.ca")) {
            return true;
        } else if (university.contains("University of Toronto") && emailid.contains("utoronto.ca")) {
            return true;
        } else if (university.contains("University of Waterloo") && emailid.contains("uwaterloo.ca")) {
            return true;
        } else if (university.contains("University of Windsor") && emailid.contains("uwindsor.ca")) {
            return true;
        } else if (university.contains("Western University") && emailid.contains("uwo.ca")) {
            return true;
        } else if (university.contains("Wilfrid Laurier University") && emailid.contains("wlu.ca")) {
            return true;
        } else if (university.contains("York University") && emailid.contains("yorku.ca")) {
            return true;
        } else
        {
            return false;
        }
    }
}