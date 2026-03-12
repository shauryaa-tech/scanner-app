package com.smartlead.scanner.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.smartlead.scanner.data.local.Converters;
import com.smartlead.scanner.data.local.entity.LeadEntity;
import com.smartlead.scanner.domain.model.LeadPriority;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LeadDao_Impl implements LeadDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LeadEntity> __insertionAdapterOfLeadEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<LeadEntity> __deletionAdapterOfLeadEntity;

  private final EntityDeletionOrUpdateAdapter<LeadEntity> __updateAdapterOfLeadEntity;

  public LeadDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLeadEntity = new EntityInsertionAdapter<LeadEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `leads` (`id`,`name`,`designation`,`company`,`phone`,`email`,`website`,`overallConfidence`,`timestamp`,`street`,`area`,`city`,`state`,`postalCode`,`country`,`description`,`priority`,`linkedIn`,`scrapedCompanyStatus`,`companyCategory`,`aiGeneratedMessage`,`scrapedDataJson`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LeadEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getDesignation() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDesignation());
        }
        if (entity.getCompany() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCompany());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPhone());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getEmail());
        }
        if (entity.getWebsite() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getWebsite());
        }
        statement.bindLong(8, entity.getOverallConfidence());
        statement.bindLong(9, entity.getTimestamp());
        if (entity.getStreet() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getStreet());
        }
        if (entity.getArea() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getArea());
        }
        if (entity.getCity() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getCity());
        }
        if (entity.getState() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getState());
        }
        if (entity.getPostalCode() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getPostalCode());
        }
        if (entity.getCountry() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getCountry());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getDescription());
        }
        final String _tmp = __converters.fromPriority(entity.getPriority());
        statement.bindString(17, _tmp);
        if (entity.getLinkedIn() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getLinkedIn());
        }
        if (entity.getScrapedCompanyStatus() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getScrapedCompanyStatus());
        }
        if (entity.getCompanyCategory() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getCompanyCategory());
        }
        if (entity.getAiGeneratedMessage() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getAiGeneratedMessage());
        }
        if (entity.getScrapedDataJson() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getScrapedDataJson());
        }
      }
    };
    this.__deletionAdapterOfLeadEntity = new EntityDeletionOrUpdateAdapter<LeadEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `leads` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LeadEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfLeadEntity = new EntityDeletionOrUpdateAdapter<LeadEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `leads` SET `id` = ?,`name` = ?,`designation` = ?,`company` = ?,`phone` = ?,`email` = ?,`website` = ?,`overallConfidence` = ?,`timestamp` = ?,`street` = ?,`area` = ?,`city` = ?,`state` = ?,`postalCode` = ?,`country` = ?,`description` = ?,`priority` = ?,`linkedIn` = ?,`scrapedCompanyStatus` = ?,`companyCategory` = ?,`aiGeneratedMessage` = ?,`scrapedDataJson` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LeadEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getDesignation() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDesignation());
        }
        if (entity.getCompany() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCompany());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPhone());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getEmail());
        }
        if (entity.getWebsite() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getWebsite());
        }
        statement.bindLong(8, entity.getOverallConfidence());
        statement.bindLong(9, entity.getTimestamp());
        if (entity.getStreet() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getStreet());
        }
        if (entity.getArea() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getArea());
        }
        if (entity.getCity() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getCity());
        }
        if (entity.getState() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getState());
        }
        if (entity.getPostalCode() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getPostalCode());
        }
        if (entity.getCountry() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getCountry());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getDescription());
        }
        final String _tmp = __converters.fromPriority(entity.getPriority());
        statement.bindString(17, _tmp);
        if (entity.getLinkedIn() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getLinkedIn());
        }
        if (entity.getScrapedCompanyStatus() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getScrapedCompanyStatus());
        }
        if (entity.getCompanyCategory() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getCompanyCategory());
        }
        if (entity.getAiGeneratedMessage() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getAiGeneratedMessage());
        }
        if (entity.getScrapedDataJson() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getScrapedDataJson());
        }
        statement.bindLong(23, entity.getId());
      }
    };
  }

  @Override
  public Object insertLead(final LeadEntity lead, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfLeadEntity.insertAndReturnId(lead);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLead(final LeadEntity lead, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfLeadEntity.handle(lead);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLead(final LeadEntity lead, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfLeadEntity.handle(lead);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<LeadEntity>> getAllLeads() {
    final String _sql = "SELECT * FROM leads ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"leads"}, new Callable<List<LeadEntity>>() {
      @Override
      @NonNull
      public List<LeadEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDesignation = CursorUtil.getColumnIndexOrThrow(_cursor, "designation");
          final int _cursorIndexOfCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfOverallConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "overallConfidence");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfStreet = CursorUtil.getColumnIndexOrThrow(_cursor, "street");
          final int _cursorIndexOfArea = CursorUtil.getColumnIndexOrThrow(_cursor, "area");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfPostalCode = CursorUtil.getColumnIndexOrThrow(_cursor, "postalCode");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfLinkedIn = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedIn");
          final int _cursorIndexOfScrapedCompanyStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "scrapedCompanyStatus");
          final int _cursorIndexOfCompanyCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "companyCategory");
          final int _cursorIndexOfAiGeneratedMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGeneratedMessage");
          final int _cursorIndexOfScrapedDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "scrapedDataJson");
          final List<LeadEntity> _result = new ArrayList<LeadEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LeadEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDesignation;
            if (_cursor.isNull(_cursorIndexOfDesignation)) {
              _tmpDesignation = null;
            } else {
              _tmpDesignation = _cursor.getString(_cursorIndexOfDesignation);
            }
            final String _tmpCompany;
            if (_cursor.isNull(_cursorIndexOfCompany)) {
              _tmpCompany = null;
            } else {
              _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            final int _tmpOverallConfidence;
            _tmpOverallConfidence = _cursor.getInt(_cursorIndexOfOverallConfidence);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpStreet;
            if (_cursor.isNull(_cursorIndexOfStreet)) {
              _tmpStreet = null;
            } else {
              _tmpStreet = _cursor.getString(_cursorIndexOfStreet);
            }
            final String _tmpArea;
            if (_cursor.isNull(_cursorIndexOfArea)) {
              _tmpArea = null;
            } else {
              _tmpArea = _cursor.getString(_cursorIndexOfArea);
            }
            final String _tmpCity;
            if (_cursor.isNull(_cursorIndexOfCity)) {
              _tmpCity = null;
            } else {
              _tmpCity = _cursor.getString(_cursorIndexOfCity);
            }
            final String _tmpState;
            if (_cursor.isNull(_cursorIndexOfState)) {
              _tmpState = null;
            } else {
              _tmpState = _cursor.getString(_cursorIndexOfState);
            }
            final String _tmpPostalCode;
            if (_cursor.isNull(_cursorIndexOfPostalCode)) {
              _tmpPostalCode = null;
            } else {
              _tmpPostalCode = _cursor.getString(_cursorIndexOfPostalCode);
            }
            final String _tmpCountry;
            if (_cursor.isNull(_cursorIndexOfCountry)) {
              _tmpCountry = null;
            } else {
              _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LeadPriority _tmpPriority;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPriority);
            _tmpPriority = __converters.toPriority(_tmp);
            final String _tmpLinkedIn;
            if (_cursor.isNull(_cursorIndexOfLinkedIn)) {
              _tmpLinkedIn = null;
            } else {
              _tmpLinkedIn = _cursor.getString(_cursorIndexOfLinkedIn);
            }
            final String _tmpScrapedCompanyStatus;
            if (_cursor.isNull(_cursorIndexOfScrapedCompanyStatus)) {
              _tmpScrapedCompanyStatus = null;
            } else {
              _tmpScrapedCompanyStatus = _cursor.getString(_cursorIndexOfScrapedCompanyStatus);
            }
            final String _tmpCompanyCategory;
            if (_cursor.isNull(_cursorIndexOfCompanyCategory)) {
              _tmpCompanyCategory = null;
            } else {
              _tmpCompanyCategory = _cursor.getString(_cursorIndexOfCompanyCategory);
            }
            final String _tmpAiGeneratedMessage;
            if (_cursor.isNull(_cursorIndexOfAiGeneratedMessage)) {
              _tmpAiGeneratedMessage = null;
            } else {
              _tmpAiGeneratedMessage = _cursor.getString(_cursorIndexOfAiGeneratedMessage);
            }
            final String _tmpScrapedDataJson;
            if (_cursor.isNull(_cursorIndexOfScrapedDataJson)) {
              _tmpScrapedDataJson = null;
            } else {
              _tmpScrapedDataJson = _cursor.getString(_cursorIndexOfScrapedDataJson);
            }
            _item = new LeadEntity(_tmpId,_tmpName,_tmpDesignation,_tmpCompany,_tmpPhone,_tmpEmail,_tmpWebsite,_tmpOverallConfidence,_tmpTimestamp,_tmpStreet,_tmpArea,_tmpCity,_tmpState,_tmpPostalCode,_tmpCountry,_tmpDescription,_tmpPriority,_tmpLinkedIn,_tmpScrapedCompanyStatus,_tmpCompanyCategory,_tmpAiGeneratedMessage,_tmpScrapedDataJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLeadById(final int id, final Continuation<? super LeadEntity> $completion) {
    final String _sql = "SELECT * FROM leads WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<LeadEntity>() {
      @Override
      @Nullable
      public LeadEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDesignation = CursorUtil.getColumnIndexOrThrow(_cursor, "designation");
          final int _cursorIndexOfCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfOverallConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "overallConfidence");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfStreet = CursorUtil.getColumnIndexOrThrow(_cursor, "street");
          final int _cursorIndexOfArea = CursorUtil.getColumnIndexOrThrow(_cursor, "area");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfPostalCode = CursorUtil.getColumnIndexOrThrow(_cursor, "postalCode");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfLinkedIn = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedIn");
          final int _cursorIndexOfScrapedCompanyStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "scrapedCompanyStatus");
          final int _cursorIndexOfCompanyCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "companyCategory");
          final int _cursorIndexOfAiGeneratedMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGeneratedMessage");
          final int _cursorIndexOfScrapedDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "scrapedDataJson");
          final LeadEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDesignation;
            if (_cursor.isNull(_cursorIndexOfDesignation)) {
              _tmpDesignation = null;
            } else {
              _tmpDesignation = _cursor.getString(_cursorIndexOfDesignation);
            }
            final String _tmpCompany;
            if (_cursor.isNull(_cursorIndexOfCompany)) {
              _tmpCompany = null;
            } else {
              _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            final int _tmpOverallConfidence;
            _tmpOverallConfidence = _cursor.getInt(_cursorIndexOfOverallConfidence);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpStreet;
            if (_cursor.isNull(_cursorIndexOfStreet)) {
              _tmpStreet = null;
            } else {
              _tmpStreet = _cursor.getString(_cursorIndexOfStreet);
            }
            final String _tmpArea;
            if (_cursor.isNull(_cursorIndexOfArea)) {
              _tmpArea = null;
            } else {
              _tmpArea = _cursor.getString(_cursorIndexOfArea);
            }
            final String _tmpCity;
            if (_cursor.isNull(_cursorIndexOfCity)) {
              _tmpCity = null;
            } else {
              _tmpCity = _cursor.getString(_cursorIndexOfCity);
            }
            final String _tmpState;
            if (_cursor.isNull(_cursorIndexOfState)) {
              _tmpState = null;
            } else {
              _tmpState = _cursor.getString(_cursorIndexOfState);
            }
            final String _tmpPostalCode;
            if (_cursor.isNull(_cursorIndexOfPostalCode)) {
              _tmpPostalCode = null;
            } else {
              _tmpPostalCode = _cursor.getString(_cursorIndexOfPostalCode);
            }
            final String _tmpCountry;
            if (_cursor.isNull(_cursorIndexOfCountry)) {
              _tmpCountry = null;
            } else {
              _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LeadPriority _tmpPriority;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPriority);
            _tmpPriority = __converters.toPriority(_tmp);
            final String _tmpLinkedIn;
            if (_cursor.isNull(_cursorIndexOfLinkedIn)) {
              _tmpLinkedIn = null;
            } else {
              _tmpLinkedIn = _cursor.getString(_cursorIndexOfLinkedIn);
            }
            final String _tmpScrapedCompanyStatus;
            if (_cursor.isNull(_cursorIndexOfScrapedCompanyStatus)) {
              _tmpScrapedCompanyStatus = null;
            } else {
              _tmpScrapedCompanyStatus = _cursor.getString(_cursorIndexOfScrapedCompanyStatus);
            }
            final String _tmpCompanyCategory;
            if (_cursor.isNull(_cursorIndexOfCompanyCategory)) {
              _tmpCompanyCategory = null;
            } else {
              _tmpCompanyCategory = _cursor.getString(_cursorIndexOfCompanyCategory);
            }
            final String _tmpAiGeneratedMessage;
            if (_cursor.isNull(_cursorIndexOfAiGeneratedMessage)) {
              _tmpAiGeneratedMessage = null;
            } else {
              _tmpAiGeneratedMessage = _cursor.getString(_cursorIndexOfAiGeneratedMessage);
            }
            final String _tmpScrapedDataJson;
            if (_cursor.isNull(_cursorIndexOfScrapedDataJson)) {
              _tmpScrapedDataJson = null;
            } else {
              _tmpScrapedDataJson = _cursor.getString(_cursorIndexOfScrapedDataJson);
            }
            _result = new LeadEntity(_tmpId,_tmpName,_tmpDesignation,_tmpCompany,_tmpPhone,_tmpEmail,_tmpWebsite,_tmpOverallConfidence,_tmpTimestamp,_tmpStreet,_tmpArea,_tmpCity,_tmpState,_tmpPostalCode,_tmpCountry,_tmpDescription,_tmpPriority,_tmpLinkedIn,_tmpScrapedCompanyStatus,_tmpCompanyCategory,_tmpAiGeneratedMessage,_tmpScrapedDataJson);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLeadByPhone(final String phone,
      final Continuation<? super LeadEntity> $completion) {
    final String _sql = "SELECT * FROM leads WHERE phone = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, phone);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<LeadEntity>() {
      @Override
      @Nullable
      public LeadEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDesignation = CursorUtil.getColumnIndexOrThrow(_cursor, "designation");
          final int _cursorIndexOfCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfOverallConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "overallConfidence");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfStreet = CursorUtil.getColumnIndexOrThrow(_cursor, "street");
          final int _cursorIndexOfArea = CursorUtil.getColumnIndexOrThrow(_cursor, "area");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfPostalCode = CursorUtil.getColumnIndexOrThrow(_cursor, "postalCode");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfLinkedIn = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedIn");
          final int _cursorIndexOfScrapedCompanyStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "scrapedCompanyStatus");
          final int _cursorIndexOfCompanyCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "companyCategory");
          final int _cursorIndexOfAiGeneratedMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGeneratedMessage");
          final int _cursorIndexOfScrapedDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "scrapedDataJson");
          final LeadEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDesignation;
            if (_cursor.isNull(_cursorIndexOfDesignation)) {
              _tmpDesignation = null;
            } else {
              _tmpDesignation = _cursor.getString(_cursorIndexOfDesignation);
            }
            final String _tmpCompany;
            if (_cursor.isNull(_cursorIndexOfCompany)) {
              _tmpCompany = null;
            } else {
              _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            final int _tmpOverallConfidence;
            _tmpOverallConfidence = _cursor.getInt(_cursorIndexOfOverallConfidence);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpStreet;
            if (_cursor.isNull(_cursorIndexOfStreet)) {
              _tmpStreet = null;
            } else {
              _tmpStreet = _cursor.getString(_cursorIndexOfStreet);
            }
            final String _tmpArea;
            if (_cursor.isNull(_cursorIndexOfArea)) {
              _tmpArea = null;
            } else {
              _tmpArea = _cursor.getString(_cursorIndexOfArea);
            }
            final String _tmpCity;
            if (_cursor.isNull(_cursorIndexOfCity)) {
              _tmpCity = null;
            } else {
              _tmpCity = _cursor.getString(_cursorIndexOfCity);
            }
            final String _tmpState;
            if (_cursor.isNull(_cursorIndexOfState)) {
              _tmpState = null;
            } else {
              _tmpState = _cursor.getString(_cursorIndexOfState);
            }
            final String _tmpPostalCode;
            if (_cursor.isNull(_cursorIndexOfPostalCode)) {
              _tmpPostalCode = null;
            } else {
              _tmpPostalCode = _cursor.getString(_cursorIndexOfPostalCode);
            }
            final String _tmpCountry;
            if (_cursor.isNull(_cursorIndexOfCountry)) {
              _tmpCountry = null;
            } else {
              _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LeadPriority _tmpPriority;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPriority);
            _tmpPriority = __converters.toPriority(_tmp);
            final String _tmpLinkedIn;
            if (_cursor.isNull(_cursorIndexOfLinkedIn)) {
              _tmpLinkedIn = null;
            } else {
              _tmpLinkedIn = _cursor.getString(_cursorIndexOfLinkedIn);
            }
            final String _tmpScrapedCompanyStatus;
            if (_cursor.isNull(_cursorIndexOfScrapedCompanyStatus)) {
              _tmpScrapedCompanyStatus = null;
            } else {
              _tmpScrapedCompanyStatus = _cursor.getString(_cursorIndexOfScrapedCompanyStatus);
            }
            final String _tmpCompanyCategory;
            if (_cursor.isNull(_cursorIndexOfCompanyCategory)) {
              _tmpCompanyCategory = null;
            } else {
              _tmpCompanyCategory = _cursor.getString(_cursorIndexOfCompanyCategory);
            }
            final String _tmpAiGeneratedMessage;
            if (_cursor.isNull(_cursorIndexOfAiGeneratedMessage)) {
              _tmpAiGeneratedMessage = null;
            } else {
              _tmpAiGeneratedMessage = _cursor.getString(_cursorIndexOfAiGeneratedMessage);
            }
            final String _tmpScrapedDataJson;
            if (_cursor.isNull(_cursorIndexOfScrapedDataJson)) {
              _tmpScrapedDataJson = null;
            } else {
              _tmpScrapedDataJson = _cursor.getString(_cursorIndexOfScrapedDataJson);
            }
            _result = new LeadEntity(_tmpId,_tmpName,_tmpDesignation,_tmpCompany,_tmpPhone,_tmpEmail,_tmpWebsite,_tmpOverallConfidence,_tmpTimestamp,_tmpStreet,_tmpArea,_tmpCity,_tmpState,_tmpPostalCode,_tmpCountry,_tmpDescription,_tmpPriority,_tmpLinkedIn,_tmpScrapedCompanyStatus,_tmpCompanyCategory,_tmpAiGeneratedMessage,_tmpScrapedDataJson);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<LeadEntity> getLeadByIdFlow(final int id) {
    final String _sql = "SELECT * FROM leads WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"leads"}, new Callable<LeadEntity>() {
      @Override
      @Nullable
      public LeadEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDesignation = CursorUtil.getColumnIndexOrThrow(_cursor, "designation");
          final int _cursorIndexOfCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfOverallConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "overallConfidence");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfStreet = CursorUtil.getColumnIndexOrThrow(_cursor, "street");
          final int _cursorIndexOfArea = CursorUtil.getColumnIndexOrThrow(_cursor, "area");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfPostalCode = CursorUtil.getColumnIndexOrThrow(_cursor, "postalCode");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfLinkedIn = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedIn");
          final int _cursorIndexOfScrapedCompanyStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "scrapedCompanyStatus");
          final int _cursorIndexOfCompanyCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "companyCategory");
          final int _cursorIndexOfAiGeneratedMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "aiGeneratedMessage");
          final int _cursorIndexOfScrapedDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "scrapedDataJson");
          final LeadEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDesignation;
            if (_cursor.isNull(_cursorIndexOfDesignation)) {
              _tmpDesignation = null;
            } else {
              _tmpDesignation = _cursor.getString(_cursorIndexOfDesignation);
            }
            final String _tmpCompany;
            if (_cursor.isNull(_cursorIndexOfCompany)) {
              _tmpCompany = null;
            } else {
              _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            final int _tmpOverallConfidence;
            _tmpOverallConfidence = _cursor.getInt(_cursorIndexOfOverallConfidence);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpStreet;
            if (_cursor.isNull(_cursorIndexOfStreet)) {
              _tmpStreet = null;
            } else {
              _tmpStreet = _cursor.getString(_cursorIndexOfStreet);
            }
            final String _tmpArea;
            if (_cursor.isNull(_cursorIndexOfArea)) {
              _tmpArea = null;
            } else {
              _tmpArea = _cursor.getString(_cursorIndexOfArea);
            }
            final String _tmpCity;
            if (_cursor.isNull(_cursorIndexOfCity)) {
              _tmpCity = null;
            } else {
              _tmpCity = _cursor.getString(_cursorIndexOfCity);
            }
            final String _tmpState;
            if (_cursor.isNull(_cursorIndexOfState)) {
              _tmpState = null;
            } else {
              _tmpState = _cursor.getString(_cursorIndexOfState);
            }
            final String _tmpPostalCode;
            if (_cursor.isNull(_cursorIndexOfPostalCode)) {
              _tmpPostalCode = null;
            } else {
              _tmpPostalCode = _cursor.getString(_cursorIndexOfPostalCode);
            }
            final String _tmpCountry;
            if (_cursor.isNull(_cursorIndexOfCountry)) {
              _tmpCountry = null;
            } else {
              _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final LeadPriority _tmpPriority;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPriority);
            _tmpPriority = __converters.toPriority(_tmp);
            final String _tmpLinkedIn;
            if (_cursor.isNull(_cursorIndexOfLinkedIn)) {
              _tmpLinkedIn = null;
            } else {
              _tmpLinkedIn = _cursor.getString(_cursorIndexOfLinkedIn);
            }
            final String _tmpScrapedCompanyStatus;
            if (_cursor.isNull(_cursorIndexOfScrapedCompanyStatus)) {
              _tmpScrapedCompanyStatus = null;
            } else {
              _tmpScrapedCompanyStatus = _cursor.getString(_cursorIndexOfScrapedCompanyStatus);
            }
            final String _tmpCompanyCategory;
            if (_cursor.isNull(_cursorIndexOfCompanyCategory)) {
              _tmpCompanyCategory = null;
            } else {
              _tmpCompanyCategory = _cursor.getString(_cursorIndexOfCompanyCategory);
            }
            final String _tmpAiGeneratedMessage;
            if (_cursor.isNull(_cursorIndexOfAiGeneratedMessage)) {
              _tmpAiGeneratedMessage = null;
            } else {
              _tmpAiGeneratedMessage = _cursor.getString(_cursorIndexOfAiGeneratedMessage);
            }
            final String _tmpScrapedDataJson;
            if (_cursor.isNull(_cursorIndexOfScrapedDataJson)) {
              _tmpScrapedDataJson = null;
            } else {
              _tmpScrapedDataJson = _cursor.getString(_cursorIndexOfScrapedDataJson);
            }
            _result = new LeadEntity(_tmpId,_tmpName,_tmpDesignation,_tmpCompany,_tmpPhone,_tmpEmail,_tmpWebsite,_tmpOverallConfidence,_tmpTimestamp,_tmpStreet,_tmpArea,_tmpCity,_tmpState,_tmpPostalCode,_tmpCountry,_tmpDescription,_tmpPriority,_tmpLinkedIn,_tmpScrapedCompanyStatus,_tmpCompanyCategory,_tmpAiGeneratedMessage,_tmpScrapedDataJson);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
