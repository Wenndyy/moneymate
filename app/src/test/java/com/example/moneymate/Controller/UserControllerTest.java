package com.example.moneymate.Controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.moneymate.Interface.MessageListener;
import com.example.moneymate.Interface.ProfileListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = {28, 30, 33})
public class UserControllerTest extends TestCase {
    private UserController userController;

    @Mock private FirebaseAuth mockAuth;
    @Mock private FirebaseFirestore mockFirestore;
    @Mock private MessageListener mockMessageListener;
    @Mock private ProfileListener mockProfileListener;
    @Mock private CollectionReference mockCollectionRef;
    @Mock private DocumentReference mockDocumentRef;
    @Mock private Task<QuerySnapshot> mockQueryTask;
    @Mock private Task<AuthResult> mockAuthTask;
    @Mock private Task<Void> mockVoidTask;
    @Mock private FirebaseUser mockUser;
    @Mock private QuerySnapshot mockQuerySnapshot;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Context context = ApplicationProvider.getApplicationContext();
        FirebaseApp.initializeApp(context);
        userController = new UserController(
                "1", "test@example.com", "John Doe", "123456789", new Date(), new Date()
        );

        userController.mAuth = mockAuth;
        userController.db = mockFirestore;
        userController.setMessageListener(mockMessageListener);
        userController.setProfileListener(mockProfileListener);


        when(mockFirestore.collection(anyString())).thenReturn(mockCollectionRef);


        Query mockQuery = mock(Query.class, RETURNS_DEEP_STUBS);
        when(mockCollectionRef.whereEqualTo(anyString(), anyString())).thenReturn(mockQuery);
        when(mockQuery.get()).thenReturn(mockQueryTask);


        when(mockQueryTask.isSuccessful()).thenReturn(true);
        when(mockQueryTask.getResult()).thenReturn(mockQuerySnapshot);
        when(mockQuerySnapshot.isEmpty()).thenReturn(true);


        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockAuth.getUid()).thenReturn("test-uid");
        when(mockCollectionRef.document(anyString())).thenReturn(mockDocumentRef);
        when(mockDocumentRef.set(any())).thenReturn(mockVoidTask);

        setupTaskCompletionCallback(mockQueryTask);
        setupTaskCompletionCallback(mockVoidTask);
    }

    private <TResult> void setupTaskCompletionCallback(Task<TResult> task) {
        doAnswer(invocation -> {
            OnCompleteListener<TResult> listener = invocation.getArgument(0);
            listener.onComplete(task);
            return task;
        }).when(task).addOnCompleteListener(any());
    }

    @Test
    public void testLogin_Success() {
        when(mockAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(mockAuthTask);
        when(mockAuthTask.isSuccessful()).thenReturn(true);
        setupTaskCompletionCallback(mockAuthTask);

        userController.login("test@example.com", "password123");

        verify(mockMessageListener).onMessageLoading(true);
        verify(mockMessageListener).onMessageSuccess("Login Success!");
        verify(mockMessageListener).onMessageLoading(false);
    }

    @Test
    public void testRegister_NewUser() {

        when(mockAuth.createUserWithEmailAndPassword(anyString(), anyString())).thenReturn(mockAuthTask);
        when(mockAuthTask.isSuccessful()).thenReturn(true);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn("test-uid");
        when(mockDocumentRef.set(any())).thenReturn(mockVoidTask);
        when(mockVoidTask.isSuccessful()).thenReturn(true);
        setupTaskCompletionCallback(mockAuthTask);
        setupTaskCompletionCallback(mockVoidTask);

        userController.register("newuser@example.com", "password123");
        verify(mockMessageListener).onMessageLoading(true);
        verify(mockCollectionRef).whereEqualTo("email", "newuser@example.com");
        verify(mockDocumentRef).set(argThat(userData -> {
            if (!(userData instanceof Map)) {
                return false;
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) userData;
            return data.get("email").equals("newuser@example.com")
                    && data.get("idUser").equals("test-uid");
        }));

        verify(mockMessageListener, timeout(5000)).onMessageSuccess("Registration Successful!");
        verify(mockMessageListener).onMessageLoading(false);
    }

    @Test
    public void testLogout_Success() {
        doNothing().when(mockAuth).signOut();

        userController.logout();

        verify(mockProfileListener).onMessageLoading(true);
        verify(mockProfileListener).onMessageLogoutSuccess("Logout Success");
        verify(mockProfileListener).onMessageLoading(false);
    }
}