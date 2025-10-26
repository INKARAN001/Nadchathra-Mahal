// Booking Stage 1 Form Handling
if (document.getElementById('stage1Form')) {
    const stage1Form = document.getElementById('stage1Form');
    stage1Form.addEventListener('submit', function(e) {
        // Store data in sessionStorage for retrieval in later stages
        const fullName = document.getElementById('fullName').value;
        const phoneNumber = document.getElementById('phoneNumber').value;
        const emailAddress = document.getElementById('emailAddress').value;
        
        sessionStorage.setItem('fullName', fullName);
        sessionStorage.setItem('phoneNumber', phoneNumber);
        sessionStorage.setItem('emailAddress', emailAddress);
    });
}

// Booking Stage 2 Form Handling
if (document.getElementById('stage2Form')) {
    const stage2Form = document.getElementById('stage2Form');
    
    // Pre-fill data from sessionStorage if available
    const urlParams = new URLSearchParams(window.location.search);
    const fullName = urlParams.get('fullName');
    const phoneNumber = urlParams.get('phoneNumber');
    const emailAddress = urlParams.get('emailAddress');
    
    if (fullName) sessionStorage.setItem('fullName', fullName);
    if (phoneNumber) sessionStorage.setItem('phoneNumber', phoneNumber);
    if (emailAddress) sessionStorage.setItem('emailAddress', emailAddress);
    
    stage2Form.addEventListener('submit', function(e) {
        const hall = document.querySelector('input[name="hall"]:checked').value;
        const bookingDate = document.getElementById('bookingDate').value;
        
        sessionStorage.setItem('hall', hall);
        sessionStorage.setItem('bookingDate', bookingDate);
    });
}

// Booking Stage 2 Form Handling and Total Calculation
if (document.getElementById('stage2Form')) {
    const stage2Form = document.getElementById('stage2Form');
    const peopleCountInput = document.getElementById('peopleCount');
    const totalAmountSpan = document.getElementById('totalAmount');
    const foodOptions = document.querySelectorAll('input[name="foodType"]');
    
    // Calculate total amount
    function calculateTotal() {
        const selectedFood = document.querySelector('input[name="foodType"]:checked');
        const peopleCount = parseInt(peopleCountInput.value) || 0;
        
        if (selectedFood && peopleCount > 0) {
            const pricePerPerson = parseFloat(selectedFood.getAttribute('data-price'));
            const total = pricePerPerson * peopleCount;
            totalAmountSpan.textContent = total.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        } else {
            totalAmountSpan.textContent = '0';
        }
    }
    
    // Add event listeners
    foodOptions.forEach(option => {
        option.addEventListener('change', calculateTotal);
    });
    
    peopleCountInput.addEventListener('input', calculateTotal);
    
    // Validate people count
    peopleCountInput.addEventListener('input', function() {
        if (this.value > 800) {
            this.value = 800;
        }
        if (this.value < 1) {
            this.value = 1;
        }
    });
    
    // Form submission validation
    stage2Form.addEventListener('submit', function(e) {
        const peopleCount = parseInt(peopleCountInput.value);
        if (peopleCount < 1 || peopleCount > 800) {
            e.preventDefault();
            alert('Please enter a valid number of people (1-800)');
            return false;
        }
        
        const selectedFood = document.querySelector('input[name="foodType"]:checked');
        if (!selectedFood) {
            e.preventDefault();
            alert('Please select a food option');
            return false;
        }
        
        const selectedPayment = document.querySelector('input[name="paymentType"]:checked');
        if (!selectedPayment) {
            e.preventDefault();
            alert('Please select a payment method');
            return false;
        }
    });
}

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

// Enhanced visual feedback for all option selections
document.addEventListener('DOMContentLoaded', function() {
    // Hall option selection
    const hallOptions = document.querySelectorAll('.hall-option input[type="radio"]');
    hallOptions.forEach(option => {
        option.addEventListener('change', function() {
            // Remove active class from all hall cards
            document.querySelectorAll('.hall-card').forEach(card => {
                card.classList.remove('selected');
            });
            // Add active class to selected card
            if (this.checked) {
                this.nextElementSibling.classList.add('selected');
            }
        });
    });

    // Food option selection
    const foodOptions = document.querySelectorAll('.food-option input[type="radio"]');
    foodOptions.forEach(option => {
        option.addEventListener('change', function() {
            // Remove active class from all food cards
            document.querySelectorAll('.food-card').forEach(card => {
                card.classList.remove('selected');
            });
            // Add active class to selected card
            if (this.checked) {
                this.nextElementSibling.classList.add('selected');
            }
        });
    });

    // Payment option selection
    const paymentOptions = document.querySelectorAll('.payment-option input[type="radio"]');
    paymentOptions.forEach(option => {
        option.addEventListener('change', function() {
            // Remove active class from all payment cards
            document.querySelectorAll('.payment-card').forEach(card => {
                card.classList.remove('selected');
            });
            // Add active class to selected card
            if (this.checked) {
                this.nextElementSibling.classList.add('selected');
            }
        });
    });

    // Add click handlers for better UX
    document.querySelectorAll('.hall-option, .food-option, .payment-option').forEach(option => {
        option.addEventListener('click', function(e) {
            // Don't trigger if clicking on the radio input directly
            if (e.target.type !== 'radio') {
                const radioInput = this.querySelector('input[type="radio"]');
                if (radioInput) {
                    radioInput.checked = true;
                    radioInput.dispatchEvent(new Event('change'));
                }
            }
        });
    });
});

