package org.datanucleus.test;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import mydomain.model.Person;

import org.datanucleus.util.NucleusLogger;
import org.junit.Test;

public class SimpleTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();

            Person person1 = new Person("1", "Mike", new Date(), "Admin", "USA");
            Person person2 = new Person("2", "Ivan", null, "Admin", "RUSSIA");
            pm.makePersistent(person1);
            pm.makePersistent(person2);

            Query query = pm.newQuery(Person.class);
            query.declareParameters("java.lang.String parameter_0, java.util.Date parameter_1");
            query.setFilter("groups.contains(parameter_0) && birthday <= parameter_1");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("parameter_0", "Admin");
            params.put("parameter_1", new Date());

            List<Person> result = (List<Person>) query.executeWithMap(params);
            junit.framework.Assert.assertEquals(result.size(), 1);
            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception thrown persisting data", thr);
            fail("Failed to persist data : " + thr.getMessage());
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        pmf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }
}
