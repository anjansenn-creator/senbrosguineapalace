document.querySelectorAll('[data-year]').forEach((el) => { el.textContent = new Date().getFullYear(); });

const menuButton = document.querySelector('.menu-toggle');
const menu = document.querySelector('.main-nav');
if (menuButton && menu) {
  menuButton.addEventListener('click', () => {
    const open = menu.classList.toggle('open');
    menuButton.setAttribute('aria-expanded', String(open));
    menuButton.setAttribute('aria-label', open ? 'Close navigation' : 'Open navigation');
    menuButton.textContent = open ? '×' : '☰';
  });
}

const enquiryForm = document.querySelector('#enquiry-form');
if (enquiryForm) {
  const interestField = document.querySelector('#interest');
  const requestedInterest = new URLSearchParams(window.location.search).get('interest');
  if (requestedInterest && interestField) {
    const matchingOption = [...interestField.options].find((option) => option.text.toLowerCase() === requestedInterest.toLowerCase());
    if (matchingOption) matchingOption.selected = true;
  }
  enquiryForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    if (!enquiryForm.reportValidity()) return;
    const status = document.querySelector('#form-status');
    const submitButton = enquiryForm.querySelector('[type="submit"]');
    const values = new FormData(enquiryForm);
    values.set('_subject', `Website enquiry — ${values.get('interest') || 'General enquiry'}`);
    values.set('_replyto', values.get('email'));
    if (status) status.textContent = 'Sending your enquiry…';
    if (submitButton) { submitButton.disabled = true; submitButton.setAttribute('aria-busy', 'true'); }
    try {
      const response = await fetch('https://formsubmit.co/ajax/anjansenn@gmail.com', {
        method: 'POST',
        headers: { Accept: 'application/json' },
        body: values
      });
      const result = await response.json();
      if (!response.ok || result.success === 'false' || result.success === false) {
        throw new Error(result.message || 'The form service could not accept your enquiry.');
      }
      enquiryForm.reset();
      if (status) status.textContent = 'Thank you — your enquiry has been sent. Our team will be in touch soon.';
    } catch (error) {
      if (status) status.textContent = 'Sorry, your enquiry could not be sent just now. Please try again or contact us at anjansenn@gmail.com.';
    } finally {
      if (submitButton) { submitButton.disabled = false; submitButton.removeAttribute('aria-busy'); }
    }
  });
}

