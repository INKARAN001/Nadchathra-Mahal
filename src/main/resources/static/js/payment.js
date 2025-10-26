// Payment Gateway Form Handling
document.addEventListener('DOMContentLoaded', function() {
    const paymentForm = document.getElementById('paymentForm');
    
    if (paymentForm) {
        const cardNumberInput = document.getElementById('cardNumber');
        const expiryDateInput = document.getElementById('expiryDate');
        const cvvInput = document.getElementById('cvv');
        const payButton = document.getElementById('payButton');
        
        // Format card number (add spaces every 4 digits)
        cardNumberInput.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\s/g, '');
            value = value.replace(/\D/g, '');
            
            let formattedValue = '';
            for (let i = 0; i < value.length; i++) {
                if (i > 0 && i % 4 === 0) {
                    formattedValue += ' ';
                }
                formattedValue += value[i];
            }
            
            e.target.value = formattedValue;
        });
        
        // Format expiry date (MM/YY)
        expiryDateInput.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            
            if (value.length >= 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 4);
            }
            
            e.target.value = value;
        });
        
        // Only allow numbers in CVV
        cvvInput.addEventListener('input', function(e) {
            e.target.value = e.target.value.replace(/\D/g, '').substring(0, 3);
        });
        
        // Card name to uppercase
        const cardNameInput = document.getElementById('cardName');
        cardNameInput.addEventListener('input', function(e) {
            e.target.value = e.target.value.toUpperCase();
        });
        
        // Form submission with loading state
        paymentForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Validate form
            const cardNumber = cardNumberInput.value.replace(/\s/g, '');
            const expiryDate = expiryDateInput.value;
            const cvv = cvvInput.value;
            const cardName = cardNameInput.value;
            
            // Validation
            if (cardNumber.length < 16) {
                alert('Please enter a valid 16-digit card number');
                cardNumberInput.focus();
                return false;
            }
            
            if (!cardName || cardName.length < 3) {
                alert('Please enter cardholder name');
                cardNameInput.focus();
                return false;
            }
            
            if (expiryDate.length !== 5 || !expiryDate.includes('/')) {
                alert('Please enter expiry date in MM/YY format');
                expiryDateInput.focus();
                return false;
            }
            
            // Validate expiry date
            const [month, year] = expiryDate.split('/').map(num => parseInt(num));
            const currentDate = new Date();
            const currentYear = currentDate.getFullYear() % 100;
            const currentMonth = currentDate.getMonth() + 1;
            
            if (month < 1 || month > 12) {
                alert('Invalid month in expiry date');
                expiryDateInput.focus();
                return false;
            }
            
            if (year < currentYear || (year === currentYear && month < currentMonth)) {
                alert('Card has expired');
                expiryDateInput.focus();
                return false;
            }
            
            if (cvv.length !== 3) {
                alert('Please enter a valid 3-digit CVV');
                cvvInput.focus();
                return false;
            }
            
            // Show loading state
            payButton.classList.add('loading');
            payButton.disabled = true;
            
            // Simulate payment processing
            setTimeout(() => {
                // Submit the form
                paymentForm.submit();
            }, 1000);
        });
    }
});

// Auto-dismiss alerts after 5 seconds
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });
});

// Add animation to payment gateway card
document.addEventListener('DOMContentLoaded', function() {
    const paymentCard = document.querySelector('.payment-gateway-card');
    if (paymentCard) {
        paymentCard.style.transform = 'scale(0.95)';
        paymentCard.style.opacity = '0';
        
        setTimeout(() => {
            paymentCard.style.transition = 'all 0.3s ease';
            paymentCard.style.transform = 'scale(1)';
            paymentCard.style.opacity = '1';
        }, 100);
    }
});

