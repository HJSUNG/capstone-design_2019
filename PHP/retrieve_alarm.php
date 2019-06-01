<?php
// error_reporting(E_ALL);
// ini_set('display_errors',1);

// Connect to the database
include('dbcon.php');

function remove_utf8_bom($text)
{
    $bom = pack('H*','EFBBBF');
    $text = preg_replace("/^$bom/", '', $text);
    return $text;
}

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ) {
        try {
            $ID = (int)$_POST['ID'];
            $stmt = $con->prepare('SELECT * FROM MedicationAlarm WHERE UserID = :ID ORDER BY Time ASC');
            $stmt->bindParam(':ID', $ID);
            $stmt->execute();
            $stmt->setFetchMode(PDO::FETCH_BOTH);

            //$result=$stmt->fetchAll(PDO::FETCH_ASSOC);

            if( !($stmt->rowCount() > 0)) {
                echo('No Result set!');
            }

            while( $row=$stmt->fetch() ) {
                // UserID, Time
                echo( '<comma>' . remove_utf8_bom($row[1]) );
            }
        }
        catch(PDOException $e) {
            die("Retrieve error!" . $e->getMessage());
        }
    }
?>