import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        HashMap<String, Integer> coursesMap = new HashMap<>();
        HashMap<String, Integer> studentMap = new HashMap<>();

        //TODO перенести в класс SubscriptionKey
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();
        //конец куска кода для переноса

        Transaction transaction = session.beginTransaction();

        List<Course> courseList = session.createQuery("From " + Course.class.getSimpleName()).getResultList();
        List<Student> studentList = session.createQuery("From " + Student.class.getSimpleName()).getResultList();

        for (int i = 0; i < courseList.size(); i++) {
            coursesMap.put(courseList.get(i).getName(), courseList.get(i).getId());
        }
        for (int i = 0; i < studentList.size(); i++) {
            studentMap.put(studentList.get(i).getName(), studentList.get(i).getId());
        }


        List<Purchase> purchaseList = session.createQuery("From " + Purchase.class.getSimpleName()).getResultList();


        for (int i = 0; i < purchaseList.size(); i++) {
            LinkedPurchaseList lpl = new LinkedPurchaseList();
            String tempStudentName = purchaseList.get(i).getStudentName();
            String tempCourseName = purchaseList.get(i).getCourseName();
            int tempStudentId = studentMap.get(tempStudentName);
            int tempCourseId = coursesMap.get(tempCourseName);
            SubscriptionIdKey key = new SubscriptionIdKey(tempStudentId, tempCourseId);
            lpl.setId(key);
            session.saveOrUpdate(lpl);
        }
        transaction.commit();
        session.close();
        sessionFactory.close();
        }
    }
