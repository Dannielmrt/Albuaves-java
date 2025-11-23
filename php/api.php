<?php
// api.php - REST API for Birds and Sightings
// Handles GET, POST, and DELETE requests.

// 1. CONFIGURATION
// Set headers to return JSON and allow Cross-Origin requests (CORS)
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, DELETE, OPTIONS');

// 2. DATABASE CONNECTION
try {
    // Connect to SQLite DB located in the parent folder
    $db = new SQLite3('../db/albuaves.db');
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["error" => "Database connection failed"]);
    exit();
}

// 3. REQUEST HANDLING
$method = $_SERVER['REQUEST_METHOD'];
// Check if we are working with 'birds' or 'sightings' (Default: birds)
$type = $_GET['type'] ?? 'birds';

switch ($method) {
    
    // --- READ DATA ---
    case 'GET':
        if ($type === 'sightings') {
            // Fetch sightings and join with birds table to get the common name
            $sql = "SELECT s.*, b.common_name 
                    FROM sighting s 
                    LEFT JOIN birds b ON s.bird_id = b.bird_id 
                    ORDER BY s.date DESC, s.time DESC";
        } else {
            // Fetch all birds from catalog
            $sql = "SELECT * FROM birds";
        }
        
        // Execute query and format result as JSON array
        $result = $db->query($sql);
        $data = [];
        while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
            $data[] = $row;
        }
        echo json_encode($data);
        break;

    // --- CREATE DATA ---
    case 'POST':
        // Decode JSON input from request body
        $input = json_decode(file_get_contents('php://input'), true);

        if ($type === 'sightings') {
            // Insert new Sighting
            $stmt = $db->prepare("INSERT INTO sighting (bird_id, date, time, location, comments, img_url) VALUES (:bid, :d, :t, :loc, :com, :img)");
            $stmt->bindValue(':bid', $input['bird_id'], SQLITE3_INTEGER);
            $stmt->bindValue(':d', $input['date'], SQLITE3_TEXT);
            $stmt->bindValue(':t', $input['time'], SQLITE3_TEXT);
            $stmt->bindValue(':loc', $input['location'], SQLITE3_TEXT);
            $stmt->bindValue(':com', $input['comments'] ?? '', SQLITE3_TEXT);
            $stmt->bindValue(':img', $input['img_url'] ?? '', SQLITE3_TEXT);
        } else {
            // Insert new Bird species
            $stmt = $db->prepare("INSERT INTO birds (common_name, scientific_name, description, img_url) VALUES (:c, :s, :d, :i)");
            $stmt->bindValue(':c', $input['common_name'], SQLITE3_TEXT);
            $stmt->bindValue(':s', $input['scientific_name'] ?? '', SQLITE3_TEXT);
            $stmt->bindValue(':d', $input['description'] ?? '', SQLITE3_TEXT);
            $stmt->bindValue(':i', $input['img_url'] ?? '', SQLITE3_TEXT);
        }

        // Execute insertion
        if ($stmt->execute()) {
            echo json_encode(["message" => "Created successfully", "id" => $db->lastInsertRowID()]);
        } else {
            http_response_code(500);
            echo json_encode(["error" => "Failed to save data"]);
        }
        break;

    // --- DELETE DATA ---
    case 'DELETE':
        // Get ID from URL parameters
        $id = $_GET['id'] ?? null;
        
        if (!$id) {
            http_response_code(400);
            echo json_encode(["error" => "Missing ID parameter"]);
            break;
        }

        // Select table based on type
        if ($type === 'sightings') {
            $stmt = $db->prepare("DELETE FROM sighting WHERE sighting_id = :id");
        } else {
            $stmt = $db->prepare("DELETE FROM birds WHERE bird_id = :id");
        }
        
        $stmt->bindValue(':id', $id, SQLITE3_INTEGER);
        
        // Execute deletion
        if ($stmt->execute()) {
            echo json_encode(["message" => "Item deleted successfully"]);
        } else {
            http_response_code(500);
            echo json_encode(["error" => "Failed to delete item"]);
        }
        break;
        
    // --- HANDLE OPTIONS (CORS preflight) ---
    case 'OPTIONS':
        http_response_code(200);
        break;
}

// Close database connection
$db->close();
?>