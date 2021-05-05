<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ss-db";

//open connection to mysql db
$conn = mysqli_connect($servername,$username,$password,$dbname) or die("Error " . mysqli_error($conn));

//fetch table rows from mysql db
$sql = "SELECT * FROM category";
$result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));

//create an array
$categories = array();
while($row =mysqli_fetch_assoc($result))
{
    $categories[] = $row;
}
echo json_encode($categories);

//close the db connection
mysqli_close($conn);
?>