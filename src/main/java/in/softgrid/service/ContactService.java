package in.softgrid.service;

import in.softgrid.entity.Contact;
import in.softgrid.repositary.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // Method to save a contact
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    // Method to find a contact by ID
    public Optional<Contact> findContactById(Long id) {
        return contactRepository.findById(id);
    }

    // Method to delete a contact by ID
    public void deleteContact(Long contactId) {
        contactRepository.deleteById(contactId);
    }

    // Method to get all contacts (if needed)
    public List<Contact> findAllContacts() {
        return contactRepository.findAll();
    }

    // Additional methods can be added as needed
}
