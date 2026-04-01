package com.youngwon.portfolio.contact.application

import com.youngwon.portfolio.contact.ContactSubmission
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mockingDetails
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.time.ZoneId

@ExtendWith(MockitoExtension::class)
class ContactCommandServiceTests {
    @Mock
    private lateinit var contactStore: ContactStore

    @InjectMocks
    private lateinit var service: ContactCommandService

    @Test
    fun `create contact returns false when submission is null`() {
        assertFalse(service.createContact(null))
    }

    @Test
    fun `create contact persists contact with next id`() {
        val seoulZone = ZoneId.of("Asia/Seoul")
        val submission = ContactSubmission(
            name = "Youngwon",
            email = "youngwon@example.com",
            message = "Hello",
        )
        `when`(contactStore.nextId()).thenReturn(8)
        val before = LocalDateTime.now(seoulZone).minusSeconds(1)

        val result = service.createContact(submission)
        val after = LocalDateTime.now(seoulZone).plusSeconds(1)

        assertTrue(result)
        verify(contactStore).nextId()

        val saveInvocation = mockingDetails(contactStore).invocations
            .first { it.method.name == "save" }
        val actual = saveInvocation.arguments[0] as StoredContact
        assertEquals(8, actual.id)
        assertEquals("Youngwon", actual.name)
        assertEquals("youngwon@example.com", actual.email)
        assertEquals("Hello", actual.message)
        assertFalse(actual.contactDate.isBefore(before))
        assertFalse(actual.contactDate.isAfter(after))
    }
}
