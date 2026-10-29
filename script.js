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
  enquiryForm.addEventListener('submit', (event) => {
    event.preventDefault();
    if (!enquiryForm.reportValidity()) return;
    const values = new FormData(enquiryForm);
    const subject = `Website enquiry — ${values.get('interest') || 'General enquiry'}`;
    const body = [
      `Name: ${values.get('name')}`,
      `Email: ${values.get('email')}`,
      `Phone: ${values.get('phone') || 'Not provided'}`,
      `Interest: ${values.get('interest') || 'General enquiry'}`,
      '',
      'Message:',
      values.get('message')
    ].join('\n');
    window.location.href = `mailto:anjansenn@gmail.com?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;
  });
}
