package com.eipl.amcs.base.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class NextCodeRepositoryImpl implements NextCodeRepository {

    @PersistenceContext
    EntityManager em;

    /**
     *
     * @param className
     * @param pkColumnName
     * @param prefix
     * @param numberOfDigit
     * @return String
     *
     * @updatedBy Nimit Shah
     * @updatedOn - 30-06-2025
     * @update - added condition to get first bill head code with ending 201.
     */
    public String getNextCode(String className, String pkColumnName, String prefix, int numberOfDigit) {
        String nextCode = "";
        try {
            if (prefix.length() == 0) {
                nextCode = em
                        .createQuery("SELECT CAST(al." + pkColumnName + " AS string) FROM " + className
                                + " al ORDER BY al." + pkColumnName + " DESC", String.class)
                        .setMaxResults(1).getSingleResult();
            }
            if (prefix.length() >= 1) {
                if (className.equalsIgnoreCase("product")) {
                    nextCode = em
                            .createQuery("SELECT SUBSTRING(al." + pkColumnName + "," + (prefix.length() + 1) + ") FROM "
                                    + className + " al where code not like '%portal%' ORDER BY CAST( SUBSTRING(al." + pkColumnName + ","
                                    + (prefix.length() + 1) + ") AS int) DESC", String.class)
                            .setMaxResults(1).getSingleResult();
                } else if (className.equalsIgnoreCase("InsuranceDetail")){
                    nextCode = em
                            .createQuery("SELECT SUBSTRING(al." + pkColumnName + "," + (prefix.length() + 1) + ") FROM "
                                    + className + " al  where insuranceDetailCode not like '%portal%' ORDER BY CAST( SUBSTRING(al." + pkColumnName + ","
                                    + (prefix.length() + 1) + ") AS int) DESC", String.class)
                            .setMaxResults(1).getSingleResult();
                } else {
                    nextCode = em
                            .createQuery("SELECT SUBSTRING(al." + pkColumnName + "," + (prefix.length() + 1) + ") FROM "
                                    + className + " al ORDER BY CAST( SUBSTRING(al." + pkColumnName + ","
                                    + (prefix.length() + 1) + ") AS int) DESC", String.class)
                            .setMaxResults(1).getSingleResult();
                }

            }

            long temp = Long.parseLong(nextCode) + 1;
            if (numberOfDigit > 0) {
                nextCode = prefix + String.format("%0" + numberOfDigit + "d", temp);
            } else {
                nextCode = prefix + temp;
            }
            return nextCode;
        } catch (NoResultException e) {
            if (numberOfDigit > 0)
                nextCode = prefix + String.format("%0" + numberOfDigit + "d", 1);
            else
                nextCode = prefix + 1;
            return nextCode;
        } catch (Exception e) {
            if (numberOfDigit > 0) {
                nextCode = prefix + String.format("%0" + numberOfDigit + "d", 1);

                if (className.equalsIgnoreCase("billhead")) {
                    int firstBillHeadNumber = 201;
                    nextCode = prefix + String.format("%0" + numberOfDigit + "d", firstBillHeadNumber);
                }
            } else {
                nextCode = prefix + 1;
            }
            return nextCode;
        }
    }
}
