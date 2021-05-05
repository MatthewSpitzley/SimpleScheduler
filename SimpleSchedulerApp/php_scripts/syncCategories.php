<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ss-db";

//open connection to mysql db
$conn = mysqli_connect($servername,$username,$password,$dbname) or die("Error " . mysqli_error($conn));

//define structure
$category=$_POST['t1'];

//execute sql statement
$sql = "INSERT INTO category (category) VALUES ($category);";
mysqli_query($conn, $sql);
echo "Category insert success";
?>