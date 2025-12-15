package com.example.passwordmanager

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class AddCredentialViewModelTest {

    // 1. Create a "Mock" Database Repository
    // This acts exactly like the Lab 07 "UserRepository" mock.
    @Mock
    lateinit var mockRepository: CredentialRepository

    // 2. Inject the mock into the ViewModel
    @InjectMocks
    lateinit var viewModel: AddCredentialViewModel

    @Before
    fun setup() {
        // Initialize the @Mock and @InjectMocks annotations
        MockitoAnnotations.openMocks(this)
        // Manual injection if @InjectMocks fails with Kotlin (safer backup)
        viewModel = AddCredentialViewModel(mockRepository)
    }

    // --- Test Case 1: Check Empty Fields ---
    @Test
    fun save_emptyFields_returnsError() = runTest {
        // Act: Try to save with empty strings
        val result = viewModel.validateAndSave(
            id = 0,
            isEditMode = false,
            website = "",
            username = "",
            password = ""
        )

        // Assert: Expect failure message
        assertThat(result).isEqualTo("Fill all fields!")
    }

    // --- Test Case 2: Check Website Exists (Mocking Database) ---
    @Test
    fun save_websiteAlreadyExists_returnsError() = runTest {
        // Arrange: Teach the mock to say "Yes, Facebook exists"
        [cite_start]// This simulates finding the website in the database [cite: 5, 10]
        `when`(mockRepository.websiteExists("Facebook")).thenReturn(true)

        // Act: Try to save "Facebook" again
        val result = viewModel.validateAndSave(
            id = 0,
            isEditMode = false,
            website = "Facebook",
            username = "NewUser",
            password = "password123"
        )

        // Assert: Expect failure message
        assertThat(result).isEqualTo("Website already exists!")

        [cite_start]// Verify: Ensure the code actually asked the repository [cite: 22]
        verify(mockRepository).websiteExists("Facebook")
    }

    // --- Bonus: Successful Save ---
    @Test
    fun save_validInput_returnsSuccess() = runTest {
        // Arrange: Teach the mock to say "No, Google does NOT exist"
        `when`(mockRepository.websiteExists("Google")).thenReturn(false)

        // Act
        val result = viewModel.validateAndSave(
            id = 0,
            isEditMode = false,
            website = "Google",
            username = "Ahmed",
            password = "password123"
        )

        // Assert
        assertThat(result).isEqualTo("Success")

        [cite_start]// Verify that insert was called on the mock repository [cite: 22]
        verify(mockRepository).insert(any())
    }
}