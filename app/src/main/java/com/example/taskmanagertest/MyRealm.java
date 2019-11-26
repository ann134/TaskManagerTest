package com.example.taskmanagertest;


import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MyRealm {

    private static Realm realm;
    private static MyRealm instance;

    public static MyRealm getInstance() {
        if (realm == null) {
            Realm.init(App.getAppContext());
            realm = createRealm();
            instance = new MyRealm();
        }
        return instance;
    }

    private static Realm createRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("db.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        return Realm.getInstance(config);
    }

    public List<Task> getTaskList() {
        if (realm != null) {
            RealmResults<Task> result = realm.where(Task.class).findAll();
            List<Task> list = realm.copyFromRealm(result);
            return list;
        }
        return null;
    }

    public void addOrUpdateTask(Task task) {
        if (realm != null) {
            realm.beginTransaction();

            if (task.getId() == 0) {
                int newId = getNext();
                task.setId(newId);
            }

            realm.insertOrUpdate(task);
            realm.commitTransaction();
        }
    }

    private int getNext() {
        Number currentIdNum = realm.where(Task.class).max("id");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }


}
